package com.rental.property.entity;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address {
    @NotBlank(message = "Street name cannot be blank")
    @Size(max = 100, message = "Street name cannot exceed 100 characters")
    private String streetName;
    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City cannot exceed 50 characters")
    private String city;
    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State cannot exceed 50 characters")
    private String state;
    @NotNull(message = "Pin code cannot be null")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pin code must be a 6-digit number")
    private Long pinCode;
}
