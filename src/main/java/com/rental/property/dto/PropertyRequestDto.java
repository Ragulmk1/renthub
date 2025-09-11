package com.rental.property.dto;
import com.rental.property.entity.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyRequestDto {
    @Embedded
    @NotNull(message = "Address cannot be null")
    private Address address;
    @NotBlank(message = "Property type cannot be blank")
    private String propertyType;
    @NotBlank(message = "BHK cannot be blank")
    private String bhk;
    @Positive(message = "Rent amount must be positive")
    private double rentAmount;
    @NotBlank(message = "Availability status cannot be blank")
    private String availabilityStatus;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

}
