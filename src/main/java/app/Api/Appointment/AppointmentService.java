package app.Api.Appointment;

import app.Shared.Enum.AppointmentStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository repository;

    public Object create(AppointmentEntity entity) {
        assertNoConflict(entity.getDoctor().getId(), entity.getStartTime(), entity.getEndTime());
        if (entity.getStatus() == null) {
            entity.setStatus(AppointmentStatus.SCHEDULED);
        }
        return repository.save(entity);
    }

    public Page<AppointmentEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Object getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found with id " + id));
    }

    public Page<AppointmentEntity> findByPatientId(Long patientId, Pageable pageable) {
        return repository.findAllByPatient_Id(patientId, pageable);
    }

    public Page<AppointmentEntity> findByDoctorId(Long doctorId, Pageable pageable) {
        return repository.findAllByDoctor_Id(doctorId, pageable);
    }

    public Object update(Long id, AppointmentEntity newEntity) {
        return repository.findById(id).map(existing -> {
            newEntity.setId(id);
            return repository.save(newEntity);
        }).orElseThrow(() -> new EntityNotFoundException("Appointment not found with id " + id));
    }

    public Object updateStatus(Long id, AppointmentStatus status) {
        var appointment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        appointment.setStatus(status);
        return repository.save(appointment);
    }

    public Object cancelAppointmentId(Long id) {
        return updateStatus(id, AppointmentStatus.CANCELLED);
    }

    public Object rescheduleAppointmentId(Long id, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        var appointment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        assertNoConflict(appointment.getDoctor().getId(), startTime, endTime);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return repository.save(appointment);
    }

    public Object delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Appointment not found with id " + id);
        }
        repository.deleteById(id);
        return "Deleted ID " + id;
    }

    private void assertNoConflict(Long doctorId, java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        boolean conflict = repository.existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
                doctorId, endTime, startTime, AppointmentStatus.CANCELLED
        );
        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor already has an appointment in this time slot");
        }
    }
}
