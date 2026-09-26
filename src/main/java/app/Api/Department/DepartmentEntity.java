package app.Api.Department;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.Api.Doctor.DoctorEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "department")
@Data
@NoArgsConstructor


@SuperBuilder
public class DepartmentEntity extends JpaBaseEntity {

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private List<DoctorEntity> doctorEntities;
}
