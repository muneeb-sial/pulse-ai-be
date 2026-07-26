package app.Api.Patient;

import app.Api.Appointment.AppointmentEntity;
import app.Api.Auth.UserEntity;
import app.Api.MedicalHistory.MedicalHistoryEntity;
import app.Api.MedicalRecord.MedicalRecordEntity;
import app.Api.Prescription.PrescriptionEntity;
import app.Base.JpaBaseEntity;
import app.Shared.Enum.GenderEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;


//├── Appointment
//├── Prescription
//├── Diagnosis
//├── Allergy
//├── MedicalRecord
//├── LabResult
//├── VitalSign
//├── Insurance
//└── EmergencyContact

@Entity
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patient_email", columnList = "email"),
                @Index(name = "idx_patient_phone", columnList = "phone"),
                @Index(name = "idx_patient_cnic", columnList = "cnic"),
                @Index(name = "idx_patient_name", columnList = "first_name,last_name"),
                @Index(name = "idx_patient_dob", columnList = "date_of_birth")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_patient_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_patient_phone",
                        columnNames = "phone"
                ),
                @UniqueConstraint(
                        name = "uk_patient_cnic",
                        columnNames = "cnic"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PatientEntity extends JpaBaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(unique = true, length = 30)
    private String idCard;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private GenderEnum gender;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "emergency_contact_name", length = 150)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @OneToMany(mappedBy = "patient")
    private List<MedicalHistoryEntity> medicalHistoryEntities;

    @OneToMany(mappedBy = "patient")
    private List<MedicalRecordEntity> medicalRecordEntities;

    @OneToMany(mappedBy = "patient")
    private List<AppointmentEntity> appointmentEntities;

    @OneToMany(mappedBy = "patient")
    private List<PrescriptionEntity> prescriptionEntities;
}