package app.Api.Patient.Dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
}
