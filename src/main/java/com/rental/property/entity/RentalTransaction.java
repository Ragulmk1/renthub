package com.rental.property.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Rental_Transaction")
@Builder
public class RentalTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="property_id", nullable = false)
    private Property property;
    @OneToMany(mappedBy = "rentalTransaction", cascade = CascadeType.ALL)
    private List<Maintenance> maintenanceRequests = new ArrayList<>();
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
}