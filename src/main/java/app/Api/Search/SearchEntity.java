package app.Api.Search;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "search")
@Data
@NoArgsConstructor

@SuperBuilder
public class SearchEntity extends JpaBaseEntity {

}
