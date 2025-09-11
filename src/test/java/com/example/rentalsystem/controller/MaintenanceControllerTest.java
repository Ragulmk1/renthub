package com.example.rentalsystem.controller;

import com.rental.property.controller.MaintenanceController;
import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.service.MaintenanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaintenanceControllerTest {

    @Mock
    private MaintenanceService maintenanceService;

    @InjectMocks
    private MaintenanceController maintenanceController;


    private MaintenanceRequestDTO sampleMaintenanceRequestInputDTO;
    private MaintenanceRequestDTO sampleMaintenanceRequestOutputDTO;
    private MaintenanceDTO sampleMaintenanceDTO;
    private MaintenanceDTO sampleUpdatedMaintenanceDTO;
    private List<MaintenanceDTO> sampleMaintenanceList;

    @BeforeEach
    void setUp() {
        sampleMaintenanceRequestInputDTO = MaintenanceRequestDTO.builder()
                .propertyId(101L)
                .tenantId(201L)
                .issueDescription("Leaky faucet in bathroom.")
                .build();

        sampleMaintenanceRequestOutputDTO = MaintenanceRequestDTO.builder()
                .propertyId(101L)
                .tenantId(201L)
                .issueDescription("Leaky faucet in bathroom.")
                .build();

        sampleMaintenanceDTO = MaintenanceDTO.builder()
                .requestId(1L)
                .propertyId(101L)
                .tenantId(201L)
                .issueDescription("Leaky faucet in bathroom.")
                .status("Pending")
                .build();

        sampleUpdatedMaintenanceDTO = MaintenanceDTO.builder()
                .requestId(1L)
                .propertyId(101L)
                .tenantId(201L)
                .issueDescription("Leaky faucet in bathroom.")
                .status("Completed")
                .build();

        sampleMaintenanceList = Arrays.asList(
                MaintenanceDTO.builder()
                        .requestId(1L).propertyId(101L).tenantId(201L).issueDescription("Leaky faucet").status("Pending").build(),
                MaintenanceDTO.builder()
                        .requestId(2L).propertyId(102L).tenantId(201L).issueDescription("Broken window").status("Accepted").build()
        );
    }

    @Test
    void testCreateMaintenanceRequest_Success() {
        when(maintenanceService.createRequest(any(MaintenanceRequestDTO.class))).thenReturn(sampleMaintenanceRequestOutputDTO);

        ResponseEntity<MaintenanceRequestDTO> response = maintenanceController.create(sampleMaintenanceRequestInputDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(sampleMaintenanceRequestOutputDTO.getIssueDescription(), response.getBody().getIssueDescription());
        verify(maintenanceService, times(1)).createRequest(eq(sampleMaintenanceRequestInputDTO));
    }

    @Test
    void testGetMaintenanceRequestsByLandlord_Success() {
        Long landlordId = 901L;
        when(maintenanceService.getAllByLandlord(eq(landlordId))).thenReturn(sampleMaintenanceList);

        ResponseEntity<List<MaintenanceDTO>> response = maintenanceController.getByLandlord(landlordId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(sampleMaintenanceList.size(), response.getBody().size());
        assertEquals(sampleMaintenanceList.get(0).getRequestId(), response.getBody().get(0).getRequestId());
        verify(maintenanceService, times(1)).getAllByLandlord(eq(landlordId));
    }

    @Test
    void testGetMaintenanceRequestsByLandlord_NoRequestsFound() {
        Long landlordId = 902L;
        when(maintenanceService.getAllByLandlord(eq(landlordId))).thenReturn(Collections.emptyList());

        ResponseEntity<List<MaintenanceDTO>> response = maintenanceController.getByLandlord(landlordId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(maintenanceService, times(1)).getAllByLandlord(eq(landlordId));
    }

    @Test
    void testUpdateMaintenanceRequestStatus_Success() {
        Long requestId = 1L;
        String newStatus = "Completed";
        when(maintenanceService.updateStatus(eq(requestId), eq(newStatus))).thenReturn(sampleUpdatedMaintenanceDTO);

        ResponseEntity<MaintenanceDTO> response = maintenanceController.updateStatus(requestId, newStatus);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(requestId, response.getBody().getRequestId());
        assertEquals(newStatus, response.getBody().getStatus());
        verify(maintenanceService, times(1)).updateStatus(eq(requestId), eq(newStatus));
    }

    @Test
    void testGetMaintenanceRequestsByTenant_Success() {
        Long tenantId = 201L;
        when(maintenanceService.getAllByTenant(eq(tenantId))).thenReturn(sampleMaintenanceList);

        ResponseEntity<List<MaintenanceDTO>> response = maintenanceController.getByTenant(tenantId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(sampleMaintenanceList.size(), response.getBody().size());
        assertEquals(tenantId, response.getBody().get(0).getTenantId());
        assertEquals(tenantId, response.getBody().get(1).getTenantId());
        verify(maintenanceService, times(1)).getAllByTenant(eq(tenantId));
    }

    @Test
    void testGetMaintenanceRequestsByTenant_NoRequestsFound() {
        Long tenantId = 202L;
        when(maintenanceService.getAllByTenant(eq(tenantId))).thenReturn(Collections.emptyList());

        ResponseEntity<List<MaintenanceDTO>> response = maintenanceController.getByTenant(tenantId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(maintenanceService, times(1)).getAllByTenant(eq(tenantId));
    }
}