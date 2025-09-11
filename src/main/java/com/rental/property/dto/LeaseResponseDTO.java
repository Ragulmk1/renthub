
package com.rental.property.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaseResponseDTO {
    private Long leaseId;
    private String base64PdfContent;
    private Double amount;
}