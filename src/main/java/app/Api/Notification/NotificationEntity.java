package app.Api.Notification;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.Base.JpaBaseEntity;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor

@SuperBuilder
public class NotificationEntity extends JpaBaseEntity {

}
