package com.example.rentalsystem.dto;
import com.rental.property.dto.LeaseAgreementDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
public class LeaseAgreementDTOTest {
    @Test
    void testNoArgsConstructor() {
        LeaseAgreementDTO dto = new LeaseAgreementDTO();
        assertNull(dto.getLandlordName());
        assertNull(dto.getTenantName());
        assertNull(dto.getPropertyAddress());
        assertNull(dto.getStartDate());
        assertNull(dto.getEndDate());
        assertNull(dto.getPropertyId());
        assertEquals(0.0, dto.getAmount());
        assertNull(dto.getDurationInMonths());
        assertNull(dto.getAgreementDate());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getTransactionId());
        assertNull(dto.getNewStartDate());
        assertNull(dto.getNewEndDate());
    }
    @Test
    void testAllArgsConstructor() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDateTime createdAt = LocalDateTime.now();
        LeaseAgreementDTO dto = new LeaseAgreementDTO(
                "Jane Smith",
                "John Doe",
                "123 Main St",
                startDate,
                endDate,
                1L,
                1500.0,
                12L,
                "2024-12-01",
                createdAt,
                100L,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31)
        );
        assertEquals("Jane Smith", dto.getLandlordName());
        assertEquals("John Doe", dto.getTenantName());
        assertEquals("123 Main St", dto.getPropertyAddress());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(1L, dto.getPropertyId());
        assertEquals(1500.0, dto.getAmount());
        assertEquals(12L, dto.getDurationInMonths());
        assertEquals("2024-12-01", dto.getAgreementDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(100L, dto.getTransactionId());
        assertEquals(LocalDate.of(2026, 1, 1), dto.getNewStartDate());
        assertEquals(LocalDate.of(2026, 12, 31), dto.getNewEndDate());
    }
    @Test
    void testBuilder() {
        LocalDate startDate = LocalDate.of(2025, 2, 1);
        LocalDate endDate = LocalDate.of(2026, 2, 1);
        LocalDateTime createdAt = LocalDateTime.now().minusDays(5);
        LeaseAgreementDTO dto = LeaseAgreementDTO.builder()
                .landlordName("Alice Brown")
                .tenantName("Bob Green")
                .propertyAddress("456 Oak Ave")
                .startDate(startDate)
                .endDate(endDate)
                .propertyId(2L)
                .amount(2000.50)
                .durationInMonths(24L)
                .agreementDate("2025-01-15")
                .createdAt(createdAt)
                .transactionId(200L)
                .newStartDate(LocalDate.of(2027, 2, 1))
                .newEndDate(LocalDate.of(2028, 2, 1))
                .build();
        assertEquals("Alice Brown", dto.getLandlordName());
        assertEquals("Bob Green", dto.getTenantName());
        assertEquals("456 Oak Ave", dto.getPropertyAddress());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(2L, dto.getPropertyId());
        assertEquals(2000.50, dto.getAmount());
        assertEquals(24L, dto.getDurationInMonths());
        assertEquals("2025-01-15", dto.getAgreementDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(200L, dto.getTransactionId());
        assertEquals(LocalDate.of(2027, 2, 1), dto.getNewStartDate());
        assertEquals(LocalDate.of(2028, 2, 1), dto.getNewEndDate());
    }
    @Test
    void testConstructorWithRequiredFields() {
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 1);
        LeaseAgreementDTO dto = new LeaseAgreementDTO(
                "Charlie White",
                "Diana Black",
                "789 Pine Ln",
                startDate,
                endDate,
                3L,
                1800.75
        );
        assertEquals("Charlie White", dto.getLandlordName());
        assertEquals("Diana Black", dto.getTenantName());
        assertEquals("789 Pine Ln", dto.getPropertyAddress());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(3L, dto.getPropertyId());
        assertEquals(1800.75, dto.getAmount());
        assertNull(dto.getDurationInMonths());
        assertNull(dto.getAgreementDate());
        assertNull(dto.getCreatedAt());
        assertNull(dto.getTransactionId());
        assertNull(dto.getNewStartDate());
        assertNull(dto.getNewEndDate());
    }
    @Test
    void testSettersAndGetters() {
        LeaseAgreementDTO dto = new LeaseAgreementDTO();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusYears(1);
        LocalDateTime createdAt = LocalDateTime.now();
        dto.setLandlordName("Eve Grey");
        dto.setTenantName("Frank Red");
        dto.setPropertyAddress("101 Elm St");
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setPropertyId(4L);
        dto.setAmount(2250.0);
        dto.setDurationInMonths(36L);
        dto.setAgreementDate("2025-05-15");
        dto.setCreatedAt(createdAt);
        dto.setTransactionId(300L);
        dto.setNewStartDate(LocalDate.now().plusYears(2));
        dto.setNewEndDate(LocalDate.now().plusYears(3));
        assertEquals("Eve Grey", dto.getLandlordName());
        assertEquals("Frank Red", dto.getTenantName());
        assertEquals("101 Elm St", dto.getPropertyAddress());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(4L, dto.getPropertyId());
        assertEquals(2250.0, dto.getAmount());
        assertEquals(36L, dto.getDurationInMonths());
        assertEquals("2025-05-15", dto.getAgreementDate());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(300L, dto.getTransactionId());
        assertEquals(LocalDate.now().plusYears(2), dto.getNewStartDate());
        assertEquals(LocalDate.now().plusYears(3), dto.getNewEndDate());
    }
}