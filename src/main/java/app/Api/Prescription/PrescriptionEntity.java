package app.Api.Prescription;

import app.Api.Appointment.AppointmentEntity;
import app.Api.Doctor.DoctorEntity;
import app.Api.Patient.PatientEntity;
import app.Base.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(
        name = "prescriptions",
        indexes = {
                @Index(name = "idx_prescription_patient", columnList = "patient_id"),
                @Index(name = "idx_prescription_doctor", columnList = "doctor_id"),
                @Index(name = "idx_prescription_appointment", columnList = "appointment_id"),
                @Index(name = "idx_prescription_date", columnList = "prescribed_date")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PrescriptionEntity extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_patient")
    )
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "doctor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_prescription_doctor")
    )
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "appointment_id",
            foreignKey = @ForeignKey(name = "fk_prescription_appointment")
    )
    private AppointmentEntity appointment;

    @Column(name = "prescribed_date", nullable = false)
    private LocalDate prescribedDate;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "prescription" )
    private List<PrescriptionItemEntity> prescriptionItemEntities;
}