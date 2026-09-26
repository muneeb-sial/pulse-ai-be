package app.Api.Patient.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class PatientCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String phone;

    @NotBlank
    private String idCard;

    /**
     * Optional login for the patient. `patients.user_id` is NOT NULL and UNIQUE, so every
     * patient needs its own user row; when this is omitted one is provisioned from the phone
     * number instead. See PatientService.resolveUser.
     */
    @Email(message = "Email must be an email")
    private String email;
}
