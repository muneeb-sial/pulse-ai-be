package app.Api.Department;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor


@SuperBuilder
public class DepartmentEntity extends JpaBaseEntity {

}
