package app.Api.Notification;

import app.Api.Auth.UserEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    private String description;

}
