package app.Api.Auth.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class AuthResetPasswordDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
