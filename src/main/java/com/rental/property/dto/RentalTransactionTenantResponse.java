package com.rental.property.dto;
import lombok.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalTransactionTenantResponse {
    Long propertyId;
    Long transactionId;
    String address;
    String bhk;
    String description;
    String status;
    byte [] image;
}
