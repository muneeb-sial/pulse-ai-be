package app.Seeder.step;

import app.Api.Appointment.AppointmentEntity;
import app.Api.MedicalHistory.MedicalHistoryEntity;
import app.Api.MedicalRecord.MedicalRecordEntity;
import app.Api.MedicalRecord.MedicalRecordRepository;
import app.Shared.Enum.AppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** One record per COMPLETED appointment (the table allows at most one per appointment). */
@Component
@Order(80)
@RequiredArgsConstructor
public class MedicalRecordSeeder implements SeedStep {

    private final MedicalRecordRepository records;
    private final SeedBatch batch;

    @Override
    public String name() {
        return "medical records";
    }

    @Override
    public int run(SeedContext ctx) {
        List<MedicalRecordEntity> list = new ArrayList<>();
        for (AppointmentEntity a : ctx.getAppointments()) {
            if (a.getStatus() != AppointmentStatus.COMPLETED) {
                continue;
            }
            List<MedicalHistoryEntity> patientHistories = ctx.getHistoriesByPatientId().get(a.getPatient().getId());
            if (patientHistories == null || patientHistories.isEmpty()) {
                continue;
            }
            list.add(MedicalRecordEntity.builder()
                    .patient(a.getPatient())
                    .doctor(a.getDoctor())
                    .appointment(a)
                    .medicalHistory(ctx.pick(patientHistories))
                    .chiefComplaint(ctx.detail())
                    .diagnosis(ctx.detail())
                    .examination(ctx.detail())
                    .treatmentPlan(ctx.detail())
                    .notes(ctx.note())
                    .build());
        }
        batch.saveAll(records, list);
        return list.size();
    }
}
