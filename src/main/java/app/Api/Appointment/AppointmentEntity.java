package app.Api.Appointment;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@SuperBuilder
public class AppointmentEntity extends JpaBaseEntity {

}
