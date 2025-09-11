package com.example.rentalsystem.controller;

import com.rental.property.controller.LeaseController;
import com.rental.property.dto.LeaseAgreementDTO;
import com.rental.property.dto.LeaseResponseDTO;
import com.rental.property.service.LeaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LeaseControllerTest {

    @InjectMocks
    private LeaseController leaseController;

    @Mock
    private LeaseService leaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void previewLeaseAgreement_shouldReturnPdfData() {
//        // Arrange
//        Long transactionId = 1L;
//        byte[] pdfData = "mock pdf data".getBytes();
//        when(leaseService.generateLeasePreview(transactionId)).thenReturn(pdfData);
//
//        // Act
//        ResponseEntity<byte[]> response = leaseController.previewLeaseAgreement(transactionId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(MediaType.APPLICATION_PDF, response.getContentType());
//        assertEquals("inline; filename=lease-agreement-preview.pdf", response.getHeaders().getContentDisposition().toString());
//        assertEquals(pdfData, response.getBody());
//        verify(leaseService, times(1)).generateLeasePreview(transactionId);
//    }

    @Test
    void confirmLease_shouldReturnOkWithLeaseResponseDTOOnSuccess() {
        // Arrange
        Long transactionId = 1L;
        LeaseResponseDTO responseDTO = new LeaseResponseDTO();
        when(leaseService.confirmLeaseAndGetSignedPdf(transactionId)).thenReturn(responseDTO);

        // Act
        ResponseEntity<?> response = leaseController.confirmLease(transactionId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(leaseService, times(1)).confirmLeaseAndGetSignedPdf(transactionId);
    }

    @Test
    void confirmLease_shouldReturnBadRequestOnError() {
        // Arrange
        Long transactionId = 1L;
        String errorMessage = "Lease confirmation failed";
        when(leaseService.confirmLeaseAndGetSignedPdf(transactionId)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<?> response = leaseController.confirmLease(transactionId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: " + errorMessage, response.getBody());
        verify(leaseService, times(1)).confirmLeaseAndGetSignedPdf(transactionId);
    }

//    @Test
//    void confirmLease_shouldReturnInternalServerErrorOnUnexpectedError() {
//        // Arrange
//        Long transactionId = 1L;
//        when(leaseService.confirmLeaseAndGetSignedPdf(transactionId)).thenThrow(new Exception("Unexpected error"));
//
//        // Act
//        ResponseEntity<?> response = leaseController.confirmLease(transactionId);
//
//        // Assert
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//        assertEquals("Internal server error during lease confirmation.", response.getBody());
//        verify(leaseService, times(1)).confirmLeaseAndGetSignedPdf(transactionId);
//    }

//    @Test
//    void renewLease_shouldReturnPdfData() {
//        // Arrange
//        Long transactionId = 1L;
//        byte[] renewedPdf = "renewed pdf data".getBytes();
//        when(leaseService.renewLease(transactionId)).thenReturn(renewedPdf);
//
//        // Act
//        ResponseEntity<byte[]> response = leaseController.renewLease(transactionId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(MediaType.APPLICATION_PDF, response.getContentType());
//        assertEquals("attachment; filename=renewed-lease.pdf", response.getHeaders().getContentDisposition().toString());
//        assertEquals(renewedPdf, response.getBody());
//        verify(leaseService, times(1)).renewLease(transactionId);
//    }

    @Test
    void terminateLease_shouldReturnOkWithSuccessMessage() {
        // Arrange
        Long propertyId = 10L;
        String successMessage = "Lease for property ID 10 terminated successfully.";
        when(leaseService.terminateLease(propertyId)).thenReturn(successMessage);

        // Act
        ResponseEntity<String> response = leaseController.terminateLease(propertyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
        verify(leaseService, times(1)).terminateLease(propertyId);
    }

    @Test
    void terminateLease_shouldReturnNotFoundWithErrorMessage() {
        // Arrange
        Long propertyId = 20L;
        String errorMessage = "No active lease found for property ID 20.";
        when(leaseService.terminateLease(propertyId)).thenReturn(errorMessage);

        // Act
        ResponseEntity<String> response = leaseController.terminateLease(propertyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(leaseService, times(1)).terminateLease(propertyId);
    }

    @Test
    void getLeasesByTenantId_shouldReturnOkWithListOfLeaseAgreementDTOs() {
        // Arrange
        Long tenantId = 5L;
        List<LeaseAgreementDTO> leases = Arrays.asList(new LeaseAgreementDTO(), new LeaseAgreementDTO());
        when(leaseService.getAllActiveLeasesByTenantId(tenantId)).thenReturn(leases);

        // Act
        ResponseEntity<List<LeaseAgreementDTO>> response = leaseController.getLeasesByTenantId(tenantId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(leases, response.getBody());
        verify(leaseService, times(1)).getAllActiveLeasesByTenantId(tenantId);
    }

    @Test
    void getLeasesByTenantId_shouldReturnNoContentIfNoLeasesFound() {
        // Arrange
        Long tenantId = 5L;
        when(leaseService.getAllActiveLeasesByTenantId(tenantId)).thenReturn(List.of());

        // Act
        ResponseEntity<List<LeaseAgreementDTO>> response = leaseController.getLeasesByTenantId(tenantId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(leaseService, times(1)).getAllActiveLeasesByTenantId(tenantId);
    }

    @Test
    void getActiveLeaseDetailsByProperty_shouldReturnOkWithLeaseAgreementDTO() {
        // Arrange
        Long propertyId = 30L;
        LeaseAgreementDTO leaseDTO = new LeaseAgreementDTO();
        when(leaseService.getActiveLeaseDetailsByProperty(propertyId)).thenReturn(Optional.of(leaseDTO));

        // Act
        ResponseEntity<?> response = leaseController.getActiveLeaseDetailsByProperty(propertyId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(leaseDTO, response.getBody());
        verify(leaseService, times(1)).getActiveLeaseDetailsByProperty(propertyId);
    }

    @Test
    void getActiveLeaseDetailsByProperty_shouldReturnNotFoundIfNoActiveLease() {
        // Arrange
        Long propertyId = 30L;
        when(leaseService.getActiveLeaseDetailsByProperty(propertyId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = leaseController.getActiveLeaseDetailsByProperty(propertyId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(leaseService, times(1)).getActiveLeaseDetailsByProperty(propertyId);
    }
}