package app.api.notification;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import lombok.experimental.SuperBuilder;
import app.base.JpaBaseEntity;
import app.api.auth.UserEntity;

@Entity
@Table(
        name = "notification",
        indexes = {
                @Index(
                        name = "idx_notification_user",
                        columnList = "user_id"
                )
        }
)
@Data
@NoArgsConstructor

@SuperBuilder
public class NotificationEntity extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean read = false;

}
