package com.rental.property.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Property_Details")
public class Property  extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;
    private Long landlordId;
    @ManyToOne
    @JoinColumn(name="id")
    private User user;
    @OneToMany(mappedBy = "property")
    private List<RentalTransaction> rentalTransactionList =new ArrayList<>();
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Maintenance> maintenance = new ArrayList<>();
    @Embedded
    private Address address;
    private String propertyType;
    private String bhk;
    private double rentAmount;
    private String availabilityStatus;
    private String description;
    @Lob
    private byte[] image1;

    @Version
    private  Long  version;
}
