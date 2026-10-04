package app.api.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.AliasAction;
import org.springframework.data.elasticsearch.core.index.AliasActionParameters;
import org.springframework.data.elasticsearch.core.index.AliasActions;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.api.appointment.AppointmentRepository;
import app.api.department.DepartmentRepository;
import app.api.doctor.DoctorRepository;
import app.api.medicalhistory.MedicalHistoryRepository;
import app.api.medicalrecord.MedicalRecordRepository;
import app.api.medicine.MedicineRepository;
import app.api.patient.PatientRepository;
import app.api.prescription.PrescriptionRepository;
import app.api.search.documents.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Entity -> search document mapping and index rebuilds. Nothing else in the app calls this yet;
 * a CDC / Kafka consumer is expected to become its second caller.
 * <p>
 * Each index is a versioned physical index ({@code patients_v1712...}) behind an alias ({@code patients}),
 * so a rebuild swaps atomically and search keeps answering from the old version meanwhile.
 */
@Service
@RequiredArgsConstructor
public class SearchIndexer {

    private static final int BATCH_SIZE = 500;

    private final ElasticsearchOperations es;
    private final PatientRepository patients;
    private final DoctorRepository doctors;
    private final DepartmentRepository departments;
    private final MedicineRepository medicines;
    private final AppointmentRepository appointments;
    private final MedicalHistoryRepository histories;
    private final MedicalRecordRepository records;
    private final PrescriptionRepository prescriptions;

    /** @return doc count indexed per alias */
    @Transactional(readOnly = true)
    public Map<String, Integer> reindexAll() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("patients", reindex("patients", PatientDoc.class, patients, e -> PatientDoc.builder()
                .id(e.getId())
                .name(e.getName())
                .phone(e.getPhone())
                .city(e.getCity())
                .gender(e.getGender() == null ? null : e.getGender().name())
                .bloodGroup(e.getBloodGroup())
                .dateOfBirth(e.getDateOfBirth())
                .active(e.getActive())
                .build()));

        counts.put("doctors", reindex("doctors", DoctorDoc.class, doctors, e -> DoctorDoc.builder()
                .id(e.getId())
                .name(e.getName())
                .specialization(e.getSpecialization())
                .qualification(e.getQualification())
                .city(e.getCity())
                .biography(e.getBiography())
                .yearsOfExperience(e.getYearsOfExperience())
                .departmentId(e.getDepartment() == null ? null : e.getDepartment().getId())
                .departmentName(e.getDepartment() == null ? null : e.getDepartment().getName())
                .active(e.getActive())
                .build()));

        counts.put("departments", reindex("departments", DepartmentDoc.class, departments, e -> DepartmentDoc.builder()
                .id(e.getId())
                .name(e.getName())
                .build()));

        counts.put("medicines", reindex("medicines", MedicineDoc.class, medicines, e -> MedicineDoc.builder()
                .id(e.getId())
                .name(e.getName())
                .genericName(e.getGenericName())
                .manufacturer(e.getManufacturer())
                .strength(e.getStrength())
                .form(e.getForm())
                .build()));

        counts.put("appointments", reindex("appointments", AppointmentDoc.class, appointments, e -> AppointmentDoc.builder()
                .id(e.getId())
                .patientId(e.getPatient().getId())
                .doctorId(e.getDoctor().getId())
                .patientName(e.getPatient().getName())
                .doctorName(e.getDoctor().getName())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .status(e.getStatus().name())
                .type(e.getType().name())
                .paid(e.getPaid())
                .build()));

        counts.put("medical_histories", reindex("medical_histories", MedicalHistoryDoc.class, histories, e -> MedicalHistoryDoc.builder()
                .id(e.getId())
                .patientId(e.getPatient().getId())
                .type(e.getType().name())
                .title(e.getTitle())
                .active(e.getActive())
                .build()));

        counts.put("medical_records", reindex("medical_records", MedicalRecordDoc.class, records, e -> MedicalRecordDoc.builder()
                .id(e.getId())
                .patientId(e.getPatient().getId())
                .doctorId(e.getDoctor().getId())
                .appointmentId(e.getAppointment() == null ? null : e.getAppointment().getId())
                .medicalHistoryId(e.getMedicalHistory().getId())
                .diagnosis(e.getDiagnosis())
                .build()));

        counts.put("prescriptions", reindex("prescriptions", PrescriptionDoc.class, prescriptions, e -> PrescriptionDoc.builder()
                .id(e.getId())
                .patientId(e.getPatient().getId())
                .doctorId(e.getDoctor().getId())
                .appointmentId(e.getAppointment() == null ? null : e.getAppointment().getId())
                .prescribedDate(e.getPrescribedDate())
                .diagnosis(e.getDiagnosis())
                .medicineNames(e.getPrescriptionItemEntities() == null ? List.of()
                        : e.getPrescriptionItemEntities().stream().map(i -> i.getMedicine().getName()).toList())
                .build()));
        return counts;
    }

    private <E, D> int reindex(String alias, Class<D> docClass, JpaRepository<E, Long> repo, Function<E, D> mapper) {
        String newIndex = alias + "_v" + System.currentTimeMillis();
        IndexCoordinates target = IndexCoordinates.of(newIndex);
        IndexOperations newOps = es.indexOps(target);
        newOps.create(newOps.createSettings(docClass));
        newOps.putMapping(newOps.createMapping(docClass));

        int total = 0;
        try {
            Page<E> page;
            int pageNo = 0;
            do {
                page = repo.findAll(PageRequest.of(pageNo++, BATCH_SIZE));
                List<D> docs = new ArrayList<>(page.getNumberOfElements());
                page.forEach(e -> docs.add(mapper.apply(e)));
                if (!docs.isEmpty()) {
                    es.save(docs, target);
                    total += docs.size();
                }
            } while (page.hasNext());
            newOps.refresh();
        } catch (RuntimeException ex) {
            newOps.delete(); // never leave a half-built index behind; the old one keeps serving
            throw ex;
        }

        swapAlias(alias, newIndex);
        return total;
    }

    /** Atomically point {@code alias} at {@code newIndex} and drop the previous versions. */
    private void swapAlias(String alias, String newIndex) {
        String[] old = es.indexOps(IndexCoordinates.of(alias + "_v*")).getInformation().stream()
                .map(i -> i.getName())
                .filter(n -> !n.equals(newIndex))
                .toArray(String[]::new);

        AliasActions actions = new AliasActions(new AliasAction.Add(
                AliasActionParameters.builder().withIndices(newIndex).withAliases(alias).build()));
        if (old.length > 0) {
            actions.add(new AliasAction.RemoveIndex(AliasActionParameters.builder().withIndices(old).build()));
        }
        es.indexOps(IndexCoordinates.of(newIndex)).alias(actions);
    }
}
