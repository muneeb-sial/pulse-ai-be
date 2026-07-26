package app.Api.Doctor;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor

@SuperBuilder
public class DoctorEntity extends JpaBaseEntity {

}
