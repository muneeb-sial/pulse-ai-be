package app.Api.MedicalHistory;

import app.Api.Patient.PatientEntity;
import app.Base.JpaBaseEntity;
import app.Shared.Enum.MedicalHistoryType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "medical_histories",
        indexes = {
                @Index(name = "idx_history_patient", columnList = "patient_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MedicalHistoryEntity extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicalHistoryType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder.Default
    private Boolean active = true;
}