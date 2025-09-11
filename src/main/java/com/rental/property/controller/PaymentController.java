package com.rental.property.controller;

import com.rental.property.dto.PaymentDTO;
import com.rental.property.dto.PaymentTenantResponse;
import com.rental.property.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/tenant/payment/initiate/{leaseId}")
    public ResponseEntity<PaymentDTO> createPayment(
            @PathVariable("leaseId") Long leaseId,
            @Valid @RequestBody PaymentDTO dto,
            @RequestParam("paymentMethodId") String paymentMethodId) {
        PaymentDTO createdPayment = service.createPayment(leaseId, dto, paymentMethodId);
        return ResponseEntity.ok(createdPayment);
    }

    @GetMapping("/tenant/payment/{paymentId}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long paymentId) {
        return ResponseEntity.ok(service.getPaymentById(paymentId));
    }

    @GetMapping("/landlord/{propertyId}/history")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByProperty(@PathVariable Long propertyId) {
        List<PaymentDTO> paymentHistory = service.getAllPaymentsByPropertyId(propertyId);
        return ResponseEntity.ok(paymentHistory);
    }

    @GetMapping("/landlord/property/{propertyId}/transaction/{transactionId}/history")
    public ResponseEntity<List<PaymentDTO>> getAllPaymentsByPropertyAndTransactionId(
            @PathVariable Long propertyId,
            @PathVariable Long transactionId) {
        List<PaymentDTO> payments = service.getPaymentsByPropertyIdAndTransactionId(propertyId, transactionId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/paymentHistory/tenant/{userId}")
    public ResponseEntity<List<PaymentTenantResponse>> getAllPaymentsByUserId(@PathVariable Long userId) {
        List<PaymentTenantResponse> list = service.getAllPaymentsByUserId(userId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}