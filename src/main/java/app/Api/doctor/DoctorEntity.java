package app.api.doctor;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.base.JpaBaseEntity;
import app.api.appointment.AppointmentEntity;
import app.api.auth.UserEntity;
import app.api.department.DepartmentEntity;
import app.api.medicalhistory.MedicalHistoryEntity;
import app.api.medicalrecord.MedicalRecordEntity;
import app.api.prescription.PrescriptionEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
//
//Doctor
//├── DoctorSchedule
//├── Appointment
//├── Prescription
//├── Diagnosis
//├── MedicalRecord
//├── Leave
//└── DoctorPatient (optional)

@Entity
@Table(
        name = "doctors",
        indexes = {
                @Index(name = "idx_doctor_phone", columnList = "phone"),
                @Index(name = "idx_doctor_license", columnList = "license_number"),
                @Index(name = "idx_doctor_specialization", columnList = "specialization"),
                @Index(name = "idx_doctor_name", columnList = "name")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_phone",
                        columnNames = "phone"
                ),
                @UniqueConstraint(
                        name = "uk_doctor_license",
                        columnNames = "license_number"
                ),
                @UniqueConstraint(
                        name = "uk_doctor_user_id",
                        columnNames = "user_id"
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DoctorEntity extends JpaBaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "license_number", nullable = false, length = 50)
    private String licenseNumber;

    @Column(nullable = false, length = 100)
    private String specialization;

    @Column(length = 100)
    private String qualification;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;

    @Column(length = 1000)
    private String biography;

    @Column(nullable = false)
    @Builder.Default
    private Integer yearsOfExperience = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<MedicalRecordEntity> medicalRecordEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<AppointmentEntity> appointmentEntities;

    @JsonIgnore
    @OneToMany(mappedBy = "doctor")
    private List<PrescriptionEntity> prescriptionEntities;
}