package com.rental.property.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDTO {
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "OTP cannot be blank")
    private String otp;
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}