package com.rental.property.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "lease_id")
    private Lease lease;

    @NotNull(message = "Amount is required")
    private Double amount;

    @NotNull(message = "Date should not be null")
    private LocalDate paymentDate;

    @NotNull(message = "Status should not be null")
    private String status;

    @Embedded
    private PaymentInfo paymentInfo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}