package app.Api.Medicine;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "medicine")
@Data
@NoArgsConstructor
@SuperBuilder
public class MedicineEntity extends JpaBaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String genericName;

    private String manufacturer;

    private String strength;

    private String form;
}
