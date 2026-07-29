package app.Api.MedicalRecord;

import app.Api.Appointment.AppointmentEntity;
import app.Api.Doctor.DoctorEntity;
import app.Api.MedicalHistory.MedicalHistoryEntity;
import app.Api.Patient.PatientEntity;
import app.Base.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "medical_records",
        indexes = {
                @Index(name = "idx_record_patient", columnList = "patient_id"),
                @Index(name = "idx_record_doctor", columnList = "doctor_id"),
                @Index(name = "idx_record_appointment", columnList = "appointment_id"),
                @Index(name = "idx_record_medical_history", columnList = "medical_history_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_appointment",
                        columnNames = {"doctor_id", "appointment_id"}
                ),
                @UniqueConstraint(
                        name = "uk_patient_appointment",
                        columnNames = {"patient_id", "appointment_id"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicalRecordEntity extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private AppointmentEntity appointment;

    @Column(columnDefinition = "TEXT")
    private String chiefComplaint;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String examination;

    @Column(columnDefinition = "TEXT")
    private String treatmentPlan;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medical_history_id")
    private MedicalHistoryEntity medicalHistory;
}