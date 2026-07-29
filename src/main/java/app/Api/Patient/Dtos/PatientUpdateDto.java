package app.Api.Patient.Dtos;

import app.Shared.Enum.GenderEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientUpdateDto {

    @Size(min = 2, max = 200)
    private String name;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits."
    )
    private String phone;

    @Size(max = 30)
    private String idCard;

    @Past(message = "Date of birth must be in the past.")
    private LocalDate dateOfBirth;

    private GenderEnum gender;

    @Size(max = 255)
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Size(max = 20)
    private String postalCode;

    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "Invalid blood group."
    )
    private String bloodGroup;

    @Size(max = 150)
    private String emergencyContactName;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Emergency contact phone must be between 10 and 15 digits."
    )
    private String emergencyContactPhone;

    @Size(max = 5000)
    private String allergies;

    @Size(max = 5000)
    private String notes;

    private Boolean active;
}