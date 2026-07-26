package app.Api.MedicalRecord;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "medical_record")
@Data
@NoArgsConstructor

@SuperBuilder
public class MedicalRecordEntity extends JpaBaseEntity {

}
