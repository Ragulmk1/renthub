package com.rental.property.dto;

import com.rental.property.entity.Address;
import com.rental.property.entity.PaymentInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long leaseId;
    private Double amount;
    private LocalDate paymentDate;
    private PaymentInfo paymentInfo;
    private String status;
    private String bhk;
    private String propertyType;
    private Address address;
}