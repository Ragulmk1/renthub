package com.rental.property.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequestDTO {
    @NotBlank(message = "Identifier cannot be blank")
    private String identifier;
}
