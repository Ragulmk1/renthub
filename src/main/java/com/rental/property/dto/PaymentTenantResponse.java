package com.rental.property.dto;

import com.rental.property.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class PaymentTenantResponse {
    private String bhk;
    private String propertyType;
    private Address address;
    private Double amount;
    private LocalDate paymentDate;
    private String status;
    private String transactionId;
}