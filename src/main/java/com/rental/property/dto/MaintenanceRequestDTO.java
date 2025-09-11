package com.rental.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequestDTO {
    private String issueType;
    private String issueDescription;
    private Long propertyId;
    private Long tenantId;
}