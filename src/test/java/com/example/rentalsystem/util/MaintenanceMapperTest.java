package com.example.rentalsystem.util;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.entity.Address;
import com.rental.property.entity.Maintenance;
import com.rental.property.entity.Property;
import com.rental.property.entity.User;
import com.rental.property.enums.MaintenanceRequestStatus;
import com.rental.property.util.MaintenanceMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MaintenanceMapperTest {

    private final MaintenanceMapper mapper = new MaintenanceMapper();

    @Test
    void toDto_shouldMapEntityToDTO() {
        User tenant = User.builder().id(1L).build();
        User landlord = User.builder().id(2L).build();
        Address address = Address.builder().streetName("Street").city("City").state("State").pinCode(123L).build();
        Property property = Property.builder().propertyId(10L).address(address).bhk("2BHK").propertyType("Apartment").user(landlord).build();
        Maintenance entity = Maintenance.builder()
                .requestId(100L)
                .tenant(tenant)
                .property(property)
                .issueType("Plumbing")
                .issueDescription("Leaky faucet")
                .status(MaintenanceRequestStatus.OPEN)
                .build();

        MaintenanceDTO dto = mapper.toDto(entity);

        assertEquals(entity.getRequestId(), dto.getRequestId());
        assertEquals(entity.getTenant().getId(), dto.getTenantId());
        assertEquals(entity.getProperty().getPropertyId(), dto.getPropertyId());
        assertEquals(entity.getProperty().getUser().getId(), dto.getLandlordId());
        assertEquals(entity.getIssueType(), dto.getIssueType());
        assertEquals(entity.getProperty().getAddress(), dto.getAddress());
        assertEquals(entity.getProperty().getBhk(), dto.getBhk());
        assertEquals(entity.getProperty().getPropertyType(), dto.getPropertyType());
        assertEquals(entity.getIssueDescription(), dto.getIssueDescription());
        assertEquals(entity.getStatus().name(), dto.getStatus());
    }

    @Test
    void toRequestDto_shouldMapEntityToRequestDTO() {
        User tenant = User.builder().id(1L).build();
        Property property = Property.builder().propertyId(10L).build();
        Maintenance entity = Maintenance.builder()
                .issueType("Painting")
                .issueDescription("Need repaint")
                .property(property)
                .tenant(tenant)
                .build();

        MaintenanceRequestDTO dto = mapper.toRequestDto(entity);

        assertEquals(entity.getIssueType(), dto.getIssueType());
        assertEquals(entity.getIssueDescription(), dto.getIssueDescription());
        assertEquals(entity.getProperty().getPropertyId(), dto.getPropertyId());
        assertEquals(entity.getTenant().getId(), dto.getTenantId());
    }

    @Test
    void toRequestDto_shouldHandleNullPropertyAndTenant() {
        Maintenance entity = Maintenance.builder()
                .issueType("Cleaning")
                .issueDescription("Need cleaning")
                .build();

        MaintenanceRequestDTO dto = mapper.toRequestDto(entity);

        assertEquals(entity.getIssueType(), dto.getIssueType());
        assertEquals(entity.getIssueDescription(), dto.getIssueDescription());
        assertNull(dto.getPropertyId());
        assertNull(dto.getTenantId());
    }

    @Test
    void toEntity_shouldMapRequestDTOAndEntitiesToEntity() {
        MaintenanceRequestDTO dto = MaintenanceRequestDTO.builder()
                .issueType("Appliance Repair")
                .issueDescription("Fridge not working")
                .propertyId(10L)
                .tenantId(1L)
                .build();
        User tenant = User.builder().id(1L).build();
        Property property = Property.builder().propertyId(10L).build();

        Maintenance entity = mapper.toEntity(dto, tenant, property);

        assertEquals(dto.getIssueType(), entity.getIssueType());
        assertEquals(dto.getIssueDescription(), entity.getIssueDescription());
        assertEquals(tenant, entity.getTenant());
        assertEquals(property, entity.getProperty());
        assertEquals(MaintenanceRequestStatus.OPEN, entity.getStatus());
        assertNull(entity.getRequestId());
    }
}