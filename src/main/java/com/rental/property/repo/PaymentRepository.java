package com.rental.property.repo;

import com.rental.property.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.lease.leaseId = :leaseId AND p.paymentDate = :paymentDate")
    Optional<Payment> findPaymentByLeaseIdAndDate(@Param("leaseId") Long leaseId, @Param("paymentDate") LocalDate paymentDate);

    List<Payment> findByLease_Property_PropertyId(Long propertyId);

    @Query("SELECT p FROM Payment p WHERE p.lease.rentalTransaction.transactionId = :transactionId")
    List<Payment> findPaymentsByTransactionId(@Param("transactionId") Long transactionId);

    @Query("SELECT p FROM Payment p WHERE p.lease.property.propertyId = :propertyId AND p.lease.rentalTransaction.transactionId = :transactionId")
    List<Payment> findByLease_Property_PropertyIdAndLease_RentalTransaction_TransactionId(
            @Param("propertyId") Long propertyId,
            @Param("transactionId") Long transactionId
    );

    List<Payment> findAllByUser_Id(Long userId);
}