package app.Api.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    /**
     * WRITE_ONLY, never serialized: UserEntity is embedded in patient and doctor
     * responses (and returned wholesale by GET /auth/all), so without this every
     * authenticated caller received bcrypt hashes for every user.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String otpToken;

    @Builder.Default
    private Boolean isVerified = false;

    @Builder.Default
    private String provider = "LOCAL";

    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<NotificationEntity> notificationEntities;

}
