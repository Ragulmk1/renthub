package com.example.rentalsystem.service;

import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.dto.LeaseResponseDTO;
import com.rental.property.entity.Lease;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import com.rental.property.entity.Address;
import com.rental.property.enums.LeaseStatus;
import com.rental.property.repo.LeaseRepository;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.RentalTransactionRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.LeaseServiceImpl;
import com.rental.property.util.PdfGeneratorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeaseServiceImplTest {

    @Mock
    private RentalTransactionRepository rentalTransactionRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LeaseRepository leaseRepository;

    @InjectMocks
    private LeaseServiceImpl leaseService;

    private Lease lease;
    private RentalTransaction rentalTransaction;
    private Property property;
    private User tenant;
    private User landlord;

    @BeforeEach
    void setUp() {
        tenant = new User();
        tenant.setFirstName("John");
        tenant.setLastName("Doe");

        landlord = new User();
        landlord.setFirstName("Alice");
        landlord.setLastName("Smith");

        property = new Property();
        property.setPropertyId(1L);
        property.setUser(landlord);
        property.setAvailabilityStatus("Available");
        Address address = new Address();
        address.setStreetName("Main Street");
        address.setCity("New York");
        address.setState("NY");
        address.setPinCode(10001L);
        property.setAddress(address);

        rentalTransaction = new RentalTransaction();
        rentalTransaction.setTransactionId(1L);
        rentalTransaction.setUser(tenant);
        rentalTransaction.setProperty(property);

        rentalTransaction.setStartDate(LocalDate.now().minusMonths(6));
        rentalTransaction.setEndDate(LocalDate.now().plusMonths(6));

        lease = new Lease();
        lease.setLeaseId(1L);
        lease.setProperty(property);
        lease.setRentalTransaction(rentalTransaction);
        lease.setStatus(LeaseStatus.ACTIVE);
        lease.setCreatedAt(LocalDateTime.now());
        lease.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testConfirmLeaseAndGetSignedPdf_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(rentalTransactionRepository.findById(1L)).thenReturn(Optional.of(rentalTransaction));
        when(leaseRepository.findByRentalTransaction_TransactionId(1L)).thenReturn(Optional.empty());
        when(leaseRepository.save(any(Lease.class))).thenReturn(lease);

        LeaseResponseDTO responseDTO = leaseService.confirmLeaseAndGetSignedPdf(1L);

        assertNotNull(responseDTO);
        assertEquals(lease.getLeaseId(), responseDTO.getLeaseId());
        verify(propertyRepository, times(1)).save(property);
        verify(leaseRepository, times(1)).save(any(Lease.class));
    }


    @Test
    void testTerminateLease_Success() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(leaseRepository.findByProperty_PropertyIdAndStatus(1L, LeaseStatus.ACTIVE)).thenReturn(Optional.of(lease));

        String result = leaseService.terminateLease(1L);

        assertEquals("Lease terminated successfully", result);
        assertEquals(LeaseStatus.TERMINATED, lease.getStatus());
        assertEquals("Available", property.getAvailabilityStatus());
        verify(leaseRepository, times(1)).save(lease);
        verify(propertyRepository, times(1)).save(property);
    }

    @Test
    void testTerminateLease_NoActiveLeaseFound() {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
        when(leaseRepository.findByProperty_PropertyIdAndStatus(1L, LeaseStatus.ACTIVE)).thenReturn(Optional.empty());

        String result = leaseService.terminateLease(1L);

        assertEquals("No active lease found for property ID: 1", result);
    }

    @Test
    void testGetActiveLeaseDetailsByProperty_Success() {
        when(leaseRepository.findActiveLeaseDetailsByPropertyId(1L, LeaseStatus.ACTIVE)).thenReturn(Optional.of(new LeaseAgreementDTO()));

        Optional<LeaseAgreementDTO> result = leaseService.getActiveLeaseDetailsByProperty(1L);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetActiveLeaseDetailsByProperty_NotFound() {
        when(leaseRepository.findActiveLeaseDetailsByPropertyId(1L, LeaseStatus.ACTIVE)).thenReturn(Optional.empty());

        Optional<LeaseAgreementDTO> result = leaseService.getActiveLeaseDetailsByProperty(1L);

        assertFalse(result.isPresent());
    }
}
