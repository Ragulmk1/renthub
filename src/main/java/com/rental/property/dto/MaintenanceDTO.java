
package com.rental.property.dto;

import com.rental.property.entity.Address;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MaintenanceDTO {
    private Long requestId;
    private Long tenantId;
    private Long landlordId;
    private Long propertyId;
    private String bhk;
    private String propertyType;
    private Address address;
    private String issueType;
    private String issueDescription;
    private String status;
}