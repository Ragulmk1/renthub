package com.rental.property.service;

import com.rental.property.dto.PaymentDTO;
import com.rental.property.dto.PaymentTenantResponse;
import com.rental.property.entity.Lease;
import com.rental.property.entity.Payment;
import com.rental.property.entity.PaymentInfo;
import com.rental.property.exception.PaymentNotFoundException;
import com.rental.property.repo.LeaseRepository;
import com.rental.property.repo.PaymentRepository;
import com.rental.property.util.PaymentMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final LeaseRepository leaseRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Transactional
    public PaymentDTO createPayment(Long leaseId, PaymentDTO dto, String paymentMethodId) {
        Stripe.apiKey = stripeApiKey;

        Lease lease = leaseRepository.findById(leaseId)
                .orElseThrow(() -> new RuntimeException("Lease not found with ID: " + leaseId));

        LocalDate paymentDate = dto.getPaymentDate();
        paymentRepository.findPaymentByLeaseIdAndDate(leaseId, paymentDate)
                .ifPresent(existingPayment -> {
                    throw new RuntimeException("Duplicate payment detected! Tenant has already paid on " + paymentDate);
                });

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (dto.getAmount() * 100))
                    .setCurrency("usd")
                    .setPaymentMethod(paymentMethodId)
                    .setConfirm(true)
                    .setDescription("Rent payment for lease ID: " + leaseId)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                log.error("Payment failed with status: {}", paymentIntent.getStatus());
                throw new RuntimeException("Payment failed with status: " + paymentIntent.getStatus());
            }

            Payment payment = PaymentMapper.toEntity(dto);
            payment.setLease(lease);
            payment.setStatus("Completed");
            payment.setUser(lease.getRentalTransaction().getUser());

            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setTransactionId(paymentIntent.getId());
            paymentInfo.setGatewayStatus(paymentIntent.getStatus());
            paymentInfo.setPaymentMethod(paymentIntent.getPaymentMethodTypes().get(0));
            payment.setPaymentInfo(paymentInfo);

            payment.getLease().getRentalTransaction().setStatus("Completed");
            payment.getLease().getProperty().setAvailabilityStatus("Rented");

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Payment created successfully for lease ID: {}", leaseId);
            return PaymentMapper.toDTO(savedPayment);

        } catch (StripeException e) {
            log.error("Payment processing failed: {}", e.getMessage());
            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with ID: " + id));
        return PaymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPaymentsByPropertyId(Long propertyId) {
        List<Payment> payments = paymentRepository.findByLease_Property_PropertyId(propertyId);
        return payments.stream().map(PaymentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByPropertyIdAndTransactionId(Long propertyId, Long transactionId) {
        List<Payment> payments = paymentRepository.findByLease_Property_PropertyIdAndLease_RentalTransaction_TransactionId(propertyId, transactionId);
        return payments.stream().map(PaymentMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<PaymentTenantResponse> getAllPaymentsByUserId(Long userId) {
        List<Payment> paymentList = paymentRepository.findAllByUser_Id(userId);
        return paymentList.stream().map(PaymentMapper::toTenantResponse).collect(Collectors.toList());
    }
}