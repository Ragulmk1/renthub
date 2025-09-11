package com.rental.property.controller;

import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.dto.LeaseResponseDTO;
import com.rental.property.service.LeaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class LeaseController {

    private final LeaseService leaseService;

    @GetMapping("generate/{transactionId}")
    public ResponseEntity<byte[]> previewLeaseAgreement(@PathVariable Long transactionId) {
        byte[] pdfData = leaseService.generateLeasePreview(transactionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=lease-agreement-preview.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

    @PostMapping("/sign/{transactionId}")
    public ResponseEntity<?> confirmLease(@PathVariable Long transactionId) {
        try {
            LeaseResponseDTO response = leaseService.confirmLeaseAndGetSignedPdf(transactionId);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            log.error("Error confirming lease for transaction ID {}: {}", transactionId, e.getMessage());
            return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error confirming lease for transaction ID {}: {}", transactionId, e.getMessage());
            return new ResponseEntity<>("Internal server error during lease confirmation.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/renew/{transactionId}")
    public ResponseEntity<byte[]> renewLease(@PathVariable Long transactionId) {
        byte[] renewedPdf = leaseService.renewLease(transactionId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=renewed-lease.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(renewedPdf);
    }

    @PutMapping("/terminate/{propertyId}")
    public ResponseEntity<String> terminateLease(@PathVariable Long propertyId) {
        String result = leaseService.terminateLease(propertyId);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @GetMapping("/lease/{tenantId}")
    public ResponseEntity<List<LeaseAgreementDTO>> getLeasesByTenantId(@PathVariable Long tenantId) {
        log.warn("Accessing lease details directly by tenantId {}. Ensure this endpoint is secured by other means if exposed.", tenantId);
        List<LeaseAgreementDTO> leases = leaseService.getAllActiveLeasesByTenantId(tenantId);
        if (leases.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(leases);
    }

    @GetMapping("/leasedetails/{propertyId}")
    public ResponseEntity<?> getActiveLeaseDetailsByProperty(@PathVariable Long propertyId) {
        Optional<LeaseAgreementDTO> activeLease = leaseService.getActiveLeaseDetailsByProperty(propertyId);
        return activeLease.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
