package com.rental.property.repo;

import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.entity.Lease;
import com.rental.property.enums.LeaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaseRepository extends JpaRepository<Lease, Long> {


    Optional<Lease> findByRentalTransaction_TransactionId(Long transactionId);

    Optional<Lease> findByProperty_PropertyIdAndStatus(Long propertyId, LeaseStatus status);

    @Query("SELECT NEW com.rental.property.dto.LeaseAgreementDTO(" +
            "CONCAT(prop.user.firstName, ' ', prop.user.lastName), " +
            "CONCAT(rt.user.firstName, ' ', rt.user.lastName), " +
            "CONCAT(prop.address.streetName, ', ', prop.address.city, ', ', prop.address.state, ' - ', prop.address.pinCode), " +
            "rt.startDate, " +
            "rt.endDate, " +
            "prop.propertyId, " +
            "prop.rentAmount " +
            ") " +
            "FROM Lease l JOIN l.rentalTransaction rt JOIN l.property prop JOIN rt.user tenantUser " +
            "WHERE tenantUser.id = :tenantId AND l.status = :status")
    List<LeaseAgreementDTO> findActiveLeaseDetailsByTenantId(@Param("tenantId") Long tenantId, @Param("status") LeaseStatus status);

    @Query("SELECT NEW com.rental.property.dto.LeaseAgreementDTO(" +
            "CONCAT(prop.user.firstName, ' ', prop.user.lastName), " +
            "CONCAT(rt.user.firstName, ' ', rt.user.lastName), " +
            "CONCAT(prop.address.streetName, ', ', prop.address.city, ', ', prop.address.state, ' - ', prop.address.pinCode), " +
            "rt.startDate, " +
            "rt.endDate, " +
            "prop.propertyId, " +
            "prop.rentAmount " +
            ") " +
            "FROM Lease l JOIN l.rentalTransaction rt JOIN l.property prop " +
            "WHERE prop.propertyId = :propertyId AND l.status = :status")
    Optional<LeaseAgreementDTO> findActiveLeaseDetailsByPropertyId(@Param("propertyId") Long propertyId, @Param("status") LeaseStatus status);

}