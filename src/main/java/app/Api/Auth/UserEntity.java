package app.Api.Auth;

import app.Base.JpaBaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Table(name = "users")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserEntity extends JpaBaseEntity {


    private String name;

    @Column(
            unique = true,
            nullable = false)
    private String email;

    private String password;

    @Builder.Default
    private String provider = "LOCAL";

    @Column(nullable = false)
    @Builder.Default
    private String role = "USER";

}
