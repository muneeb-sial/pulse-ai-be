package app.Api.Prescription;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.Api.Medicine.MedicineEntity;
import app.Base.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "prescription_items",
        indexes = {
                @Index(name = "idx_prescription", columnList = "prescription_id"),
                @Index(name = "idx_medicine", columnList = "medicine_id")
        }
)
public class PrescriptionItemEntity extends JpaBaseEntity {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private PrescriptionEntity prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private MedicineEntity medicine;

    @Column(nullable = false)
    private Integer quantity;

    @Column(length = 100)
    private String dosage;

    @Column(length = 100)
    private String frequency;

    @Column(length = 100)
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String instructions;
}
