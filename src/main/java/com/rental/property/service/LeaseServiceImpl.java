
package com.rental.property.service;

import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.dto.LeaseResponseDTO;
import com.rental.property.entity.Lease;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import com.rental.property.enums.LeaseStatus;
import com.rental.property.repo.LeaseRepository;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.RentalTransactionRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.util.PdfGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeaseServiceImpl implements LeaseService {

    private final RentalTransactionRepository rentalTransactionRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final LeaseRepository leaseRepository;

    @Override
    public byte[] generateLeasePreview(Long transactionId) {
        LeaseAgreementDTO data = fetchAgreementData(transactionId);

        return PdfGeneratorUtil.generateLeaseAgreementPdf(data, false);
    }

    @Override
    @Transactional
    public LeaseResponseDTO confirmLeaseAndGetSignedPdf(Long transactionId) {
        LeaseAgreementDTO data = fetchAgreementData(transactionId);

        Property property = propertyRepository.findById(data.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + data.getPropertyId()));

        RentalTransaction rentalTransaction = rentalTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("RentalTransaction not found with ID: " + transactionId));


        Optional<Lease> existingLease = leaseRepository.findByRentalTransaction_TransactionId(transactionId);
        Lease leaseToSave;

        if (existingLease.isPresent()) {
            leaseToSave = existingLease.get();
            log.warn("Lease already exists for transaction ID {}. Updating status.", transactionId);
        } else {
            leaseToSave = new Lease();
            leaseToSave.setCreatedAt(LocalDateTime.now());
            leaseToSave.setProperty(property);
            leaseToSave.setRentalTransaction(rentalTransaction);
            property.setAvailabilityStatus("Rented");
            propertyRepository.save(property);
        }

        leaseToSave.setUpdatedAt(LocalDateTime.now());
        leaseToSave.setStatus(LeaseStatus.ACTIVE);

        Lease savedLease = leaseRepository.save(leaseToSave);



        byte[] pdfBytes = PdfGeneratorUtil.generateLeaseAgreementPdf(data, true); // Generate PDF

        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        return LeaseResponseDTO.builder()
                .leaseId(savedLease.getLeaseId())
                .base64PdfContent(base64Pdf)
                .amount(data.getAmount())
                .build();
    }

    private LeaseAgreementDTO fetchAgreementData(Long transactionId) {
        RentalTransaction transaction = rentalTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found for ID: " + transactionId));

        User tenant = transaction.getUser();
        Property property = transaction.getProperty();
        User landlord = property.getUser();

        String propertyAddress = String.format("%s, %s, %s - %d",
                property.getAddress().getStreetName(),
                property.getAddress().getCity(),
                property.getAddress().getState(),
                property.getAddress().getPinCode());

        long durationMonths = ChronoUnit.MONTHS.between(transaction.getStartDate(), transaction.getEndDate());

        return LeaseAgreementDTO.builder()
                .tenantName(tenant.getFirstName() + " " + tenant.getLastName())
                .landlordName(landlord.getFirstName() + " " + landlord.getLastName())
                .propertyAddress(propertyAddress)
                .startDate(transaction.getStartDate())
                .endDate(transaction.getEndDate())
                .durationInMonths(durationMonths)
                .amount(property.getRentAmount())
                .propertyId(property.getPropertyId())
                .createdAt(LocalDateTime.now())
                .transactionId(transactionId)
                .build();
    }

    @Override
    @Transactional
    public byte[] renewLease(Long newTransactionId) {
        LeaseAgreementDTO data = fetchAgreementData(newTransactionId);

        return PdfGeneratorUtil.generateLeaseAgreementPdf(data, true);
    }

    @Override
    public String terminateLease(Long propertyId) {
        Optional<Lease> optionalLease = leaseRepository.findByProperty_PropertyIdAndStatus(propertyId, LeaseStatus.ACTIVE);
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found with ID: " + propertyId));

        if (optionalLease.isPresent()) {
            Lease lease = optionalLease.get();
            lease.setStatus(LeaseStatus.TERMINATED);
            lease.getRentalTransaction().setStatus("TERMINATED");
            property.setAvailabilityStatus("Available");
            lease.setUpdatedAt(LocalDateTime.now());
            leaseRepository.save(lease);
            propertyRepository.save(property);
            return "Lease terminated successfully";
        } else {
            return "No active lease found for property ID: " + propertyId;
        }
    }

    @Override
    public List<LeaseAgreementDTO> getAllActiveLeasesByTenantId(Long tenantId) {
        log.info("Fetching all active leases for tenant ID: {}", tenantId);
        return leaseRepository.findActiveLeaseDetailsByTenantId(tenantId, LeaseStatus.ACTIVE);
    }

    @Override
    public Optional<LeaseAgreementDTO> getActiveLeaseDetailsByProperty(Long propertyId) {
        log.info("Fetching active lease details for property ID: {}", propertyId);
        return leaseRepository.findActiveLeaseDetailsByPropertyId(propertyId, LeaseStatus.ACTIVE);
    }
}

