package app.Api.Patient;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor

@SuperBuilder
public class PatientEntity extends JpaBaseEntity {

}
