package com.rental.property.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Long mobileNo;
    private Long role;
    @OneToMany(mappedBy = "user")
    private List<Property> propertyList=new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<RentalTransaction> rentalTransactionList =new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private List<Payment> paymentList = new ArrayList<>();

}