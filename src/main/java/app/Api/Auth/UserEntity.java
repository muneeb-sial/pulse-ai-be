package app.Api.Auth;

import app.Api.Notification.NotificationEntity;
import app.Base.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email")
        }
)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity extends JpaBaseEntity {

    @Column(nullable = false)
    private String email;

    private String password;

    private String otpToken;

    @Builder.Default
    private Boolean isVerified = false;

    @Builder.Default
    private String provider = "LOCAL";

    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";

    @OneToMany(mappedBy = "user")
    private List<NotificationEntity> notificationEntities;

}
