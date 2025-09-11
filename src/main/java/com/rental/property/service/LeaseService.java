
package com.rental.property.service;

import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.dto.LeaseResponseDTO;
import java.util.List;
import java.util.Optional;


public interface LeaseService {
    byte[] generateLeasePreview(Long transactionId);

    LeaseResponseDTO confirmLeaseAndGetSignedPdf(Long transactionId);


    byte[] renewLease(Long transactionId);

    String terminateLease(Long propertyId);
    List<LeaseAgreementDTO> getAllActiveLeasesByTenantId(Long tenantId);
    Optional<LeaseAgreementDTO> getActiveLeaseDetailsByProperty(Long propertyId);
}