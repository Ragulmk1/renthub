package com.rental.property.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "gateway_status")
    private String gatewayStatus;
    @Column(name = "payment_method")
    private String paymentMethod;
}