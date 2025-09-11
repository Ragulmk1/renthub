package com.rental.property.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaseAgreementDTO {
    private String landlordName;
    private String tenantName;
    private String propertyAddress;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long propertyId;
    private double amount;
    private Long durationInMonths;
    private String agreementDate;
    private LocalDateTime createdAt;
    private Long transactionId;
    private LocalDate newStartDate;
    private LocalDate newEndDate;
    public LeaseAgreementDTO(String landlordName, String tenantName, String propertyAddress, LocalDate startDate, LocalDate endDate, Long propertyId, double amount) {
        this.landlordName = landlordName;
        this.tenantName = tenantName;
        this.propertyAddress = propertyAddress;
        this.startDate = startDate;
        this.endDate = endDate;
        this.propertyId = propertyId;
        this.amount = amount;
    }

}