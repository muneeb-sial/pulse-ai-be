package app.Api.Auth.Dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterDto {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    private String password;
}
