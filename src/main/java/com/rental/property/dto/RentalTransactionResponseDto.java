package com.rental.property.dto;
import lombok.*;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalTransactionResponseDto {
    private String userName;
    private String mobileNo;
    private Long transactionId;
    private Long propertyId;
    private String address;
    private String bhk;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private byte[] image;
}
