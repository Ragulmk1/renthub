package com.rental.property.entity;
import com.rental.property.enums.LeaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "lease")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaseId;
    @ManyToOne
    @JoinColumn(name = "propertyId")
    private Property property;
    @ManyToOne
    @JoinColumn(name = "transaction_id", referencedColumnName = "transactionId")
    private RentalTransaction rentalTransaction;
    @OneToMany(mappedBy = "lease")
    private List<Payment> payment;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}