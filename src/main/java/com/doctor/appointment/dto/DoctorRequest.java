package com.doctor.appointment.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "National code is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "National code must be exactly 10 digits")
    @Size(min = 10, max = 10, message = "National code must be exactly 10 digits")
    private String nationalCode;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Size(max = 100, message = "Specialty must be less than 100 characters")
    private String specialty;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone number must be valid (10-15 digits, optional + prefix)")
    @Size(max = 20, message = "Phone must be less than 20 characters")
    private String phone;
}
