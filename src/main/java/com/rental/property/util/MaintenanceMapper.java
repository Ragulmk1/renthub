
package com.rental.property.util;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.entity.Maintenance;
import com.rental.property.entity.Property;
import com.rental.property.entity.User; // Import User entity
import com.rental.property.enums.MaintenanceRequestStatus;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

    public MaintenanceDTO toDto(Maintenance entity) {
        MaintenanceDTO dto = new MaintenanceDTO();
        dto.setRequestId(entity.getRequestId());
        if (entity.getTenant() != null) {
            dto.setTenantId(entity.getTenant().getId());
        }
        if (entity.getProperty() != null) {
            dto.setPropertyId(entity.getProperty().getPropertyId());
            if (entity.getProperty().getUser() != null) { // Assuming User is the landlord for the property
                dto.setLandlordId(entity.getProperty().getUser().getId());
            }
        }
        dto.setIssueType(entity.getIssueType());
        dto.setAddress(entity.getProperty().getAddress());
        dto.setBhk(entity.getProperty().getBhk());
        dto.setPropertyType(entity.getProperty().getPropertyType());
        dto.setIssueDescription(entity.getIssueDescription());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }

    public MaintenanceRequestDTO toRequestDto(Maintenance entity) {
        MaintenanceRequestDTO dto = new MaintenanceRequestDTO();
        dto.setIssueType(entity.getIssueType());
        dto.setIssueDescription(entity.getIssueDescription());
        // For a request DTO, you might also include propertyId and tenantId if needed
        if (entity.getProperty() != null) {
            dto.setPropertyId(entity.getProperty().getPropertyId());
        }
        if (entity.getTenant() != null) {
            dto.setTenantId(entity.getTenant().getId());
        }

        return dto;
    }


    public Maintenance toEntity(MaintenanceRequestDTO dto, User tenant, Property property) {
        Maintenance entity = new Maintenance();

        entity.setTenant(tenant);
        entity.setProperty(property);
        entity.setIssueType(dto.getIssueType());
        entity.setIssueDescription(dto.getIssueDescription());
        entity.setStatus(MaintenanceRequestStatus.OPEN);
        return entity;
    }
}