package app.Api.Prescription;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "prescription")
@Data
@NoArgsConstructor

@SuperBuilder
public class PrescriptionEntity extends JpaBaseEntity {

}
