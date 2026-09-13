package app.Api.Appointment;

import app.Shared.Enum.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    Page<AppointmentEntity> findAllByPatient_Id(Long patientId, Pageable pageable);

    Page<AppointmentEntity> findAllByDoctor_Id(Long doctorId, Pageable pageable);

    boolean existsByDoctor_IdAndStartTimeLessThanAndEndTimeGreaterThanAndStatusNot(
            Long doctorId, LocalDateTime endTime, LocalDateTime startTime, AppointmentStatus excludedStatus
    );
}
