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
public class PropertyResponseDto {
        private Long propertyId;
        private Long landLordId;
        @Embedded
        private Address address;
        private String propertyType;
        private String bhk;
        private double rentAmount;
        private String availabilityStatus;
        private String description;
        @Lob
        private byte[] image1;

}
