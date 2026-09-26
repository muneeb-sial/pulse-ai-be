package app.Api.Appointment;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.Api.Doctor.DoctorEntity;
import app.Api.Patient.PatientEntity;
import app.Api.Prescription.PrescriptionEntity;
import app.Base.JpaBaseEntity;
import app.Shared.Enum.AppointmentStatus;
import app.Shared.Enum.AppointmentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_patient", columnList = "patient_id"),
                @Index(name = "idx_appointment_doctor", columnList = "doctor_id"),
                @Index(name = "idx_appointment_start", columnList = "start_time"),
                @Index(name = "idx_appointment_status", columnList = "status"),
                @Index(name = "idx_appointment_doctor_start", columnList = "doctor_id,start_time"),
                @Index(name = "idx_appointment_patient_start", columnList = "patient_id,start_time")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AppointmentEntity extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_appointment_patient")
    )
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "doctor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_appointment_doctor")
    )
    private DoctorEntity doctor;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentType type;

    @Column(length = 255)
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean paid = false;

    @JsonIgnore
    @OneToMany(mappedBy = "appointment")
    private List<PrescriptionEntity> prescriptionEntityList;
}