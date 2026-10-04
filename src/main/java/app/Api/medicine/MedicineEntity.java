package app.api.medicine;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import lombok.experimental.SuperBuilder;
import app.base.JpaBaseEntity;

@Entity
@Table(
        name = "medicine",
        indexes = {
                @Index(name = "idx_medicine_name", columnList = "name"),
                @Index(name = "idx_medicine_generic_name", columnList = "generic_name"),
                @Index(name = "idx_medicine_manufacturer", columnList = "manufacturer")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_medicine_name",
                        columnNames = "name"
                )
        }
)
@Data
@NoArgsConstructor
@SuperBuilder
public class MedicineEntity extends JpaBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "generic_name" )
    private String genericName;

    private String manufacturer;

    private String strength;

    private String form;
}
