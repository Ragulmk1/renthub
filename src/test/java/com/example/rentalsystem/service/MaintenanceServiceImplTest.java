package com.example.rentalsystem.service;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.entity.Maintenance;
import com.rental.property.entity.Property;
import com.rental.property.entity.User;
import com.rental.property.enums.MaintenanceRequestStatus;
import com.rental.property.exception.ResourceNotFoundException;
import com.rental.property.repo.MaintenanceRepository;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.MaintenanceServiceImpl;
import com.rental.property.util.MaintenanceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceServiceImplTest {

    @InjectMocks
    private MaintenanceServiceImpl maintenanceService;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MaintenanceMapper mapper;

    @Test
    void createRequest_ValidInput_ReturnsMaintenanceRequestDTO() {
        MaintenanceRequestDTO requestDTO = MaintenanceRequestDTO.builder()
                .tenantId(1L)
                .propertyId(2L)
                .issueType("Plumbing")
                .issueDescription("Leaky faucet")
                .build();

        User tenant = new User();
        tenant.setId(1L);
        Property property = new Property();
        property.setPropertyId(2L);
        Maintenance maintenance = new Maintenance();
        maintenance.setRequestId(3L);
        MaintenanceRequestDTO responseDTO = MaintenanceRequestDTO.builder()
                .issueType("Plumbing")
                .issueDescription("Leaky faucet")
                .propertyId(2L)
                .tenantId(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(propertyRepository.findById(2L)).thenReturn(Optional.of(property));
        when(mapper.toRequestDto(any(Maintenance.class))).thenReturn(responseDTO);
        when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(maintenance);

        MaintenanceRequestDTO result = maintenanceService.createRequest(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getIssueType(), result.getIssueType());
        assertEquals(responseDTO.getIssueDescription(), result.getIssueDescription());
        assertEquals(responseDTO.getPropertyId(), result.getPropertyId());
        assertEquals(responseDTO.getTenantId(), result.getTenantId());
        verify(userRepository, times(1)).findById(1L);
        verify(propertyRepository, times(1)).findById(2L);
        verify(maintenanceRepository, times(1)).save(any(Maintenance.class));
        verify(mapper, times(1)).toRequestDto(any(Maintenance.class));
    }

    @Test
    void createRequest_TenantNotFound_ThrowsResourceNotFoundException() {
        MaintenanceRequestDTO requestDTO = MaintenanceRequestDTO.builder()
                .tenantId(1L)
                .propertyId(2L)
                .issueType("Plumbing")
                .issueDescription("Leaky faucet")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.createRequest(requestDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(propertyRepository, never()).findById(anyLong());
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
        verify(mapper, never()).toRequestDto(any(Maintenance.class));
    }

    @Test
    void createRequest_PropertyNotFound_ThrowsResourceNotFoundException() {
        MaintenanceRequestDTO requestDTO = MaintenanceRequestDTO.builder()
                .tenantId(1L)
                .propertyId(2L)
                .issueType("Plumbing")
                .issueDescription("Leaky faucet")
                .build();

        User tenant = new User();
        tenant.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(tenant));
        when(propertyRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.createRequest(requestDTO));
        verify(userRepository, times(1)).findById(1L);
        verify(propertyRepository, times(1)).findById(2L);
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
        verify(mapper, never()).toRequestDto(any(Maintenance.class));
    }

    @Test
    void getAllByLandlord_ExistingLandlordId_ReturnsListOfMaintenanceDTO() {
        Long landlordId = 1L;
        User landlord = new User();
        landlord.setId(landlordId);
        Property property1 = new Property();
        property1.setUser(landlord);
        Property property2 = new Property();
        property2.setUser(landlord);
        Maintenance maintenance1 = new Maintenance();
        maintenance1.setRequestId(101L);
        maintenance1.setProperty(property1);
        Maintenance maintenance2 = new Maintenance();
        maintenance2.setRequestId(102L);
        maintenance2.setProperty(property2);
        List<Maintenance> maintenanceList = Arrays.asList(maintenance1, maintenance2);
        MaintenanceDTO dto1 = MaintenanceDTO.builder().requestId(101L).build();
        MaintenanceDTO dto2 = MaintenanceDTO.builder().requestId(102L).build();
        List<MaintenanceDTO> dtoList = Arrays.asList(dto1, dto2);

        when(maintenanceRepository.findByProperty_User_Id(landlordId)).thenReturn(maintenanceList);
        when(mapper.toDto(maintenance1)).thenReturn(dto1);
        when(mapper.toDto(maintenance2)).thenReturn(dto2);

        List<MaintenanceDTO> result = maintenanceService.getAllByLandlord(landlordId);

        assertNotNull(result);
        assertEquals(maintenanceList.size(), result.size());
        verify(maintenanceRepository, times(1)).findByProperty_User_Id(landlordId);
        verify(mapper, times(maintenanceList.size())).toDto(any(Maintenance.class));
    }

    @Test
    void getAllByLandlord_NoRequestsForLandlord_ReturnsEmptyList() {
        Long landlordId = 1L;

        when(maintenanceRepository.findByProperty_User_Id(landlordId)).thenReturn(Collections.emptyList());

        List<MaintenanceDTO> result = maintenanceService.getAllByLandlord(landlordId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(maintenanceRepository, times(1)).findByProperty_User_Id(landlordId);
        verify(mapper, never()).toDto(any(Maintenance.class));
    }

    @Test
    void updateStatus_ExistingRequestId_ReturnsUpdatedMaintenanceDTO() {
        Long requestId = 1L;
        String newStatus = "IN_PROGRESS";
        Maintenance maintenance = new Maintenance();
        maintenance.setRequestId(requestId);
        maintenance.setStatus(MaintenanceRequestStatus.OPEN);
        Maintenance updatedMaintenance = new Maintenance();
        updatedMaintenance.setRequestId(requestId);
        updatedMaintenance.setStatus(MaintenanceRequestStatus.IN_PROGRESS);
        MaintenanceDTO updatedDTO = MaintenanceDTO.builder().requestId(requestId).status(MaintenanceRequestStatus.IN_PROGRESS.toString()).build();

        when(maintenanceRepository.findById(requestId)).thenReturn(Optional.of(maintenance));
        when(maintenanceRepository.save(any(Maintenance.class))).thenReturn(updatedMaintenance);
        when(mapper.toDto(updatedMaintenance)).thenReturn(updatedDTO);

        MaintenanceDTO result = maintenanceService.updateStatus(requestId, newStatus);

        assertNotNull(result);
        assertEquals(MaintenanceRequestStatus.IN_PROGRESS.toString(), result.getStatus());
        verify(maintenanceRepository, times(1)).findById(requestId);
        verify(maintenanceRepository, times(1)).save(any(Maintenance.class));
        verify(mapper, times(1)).toDto(updatedMaintenance);
    }

    @Test
    void updateStatus_NonExistingRequestId_ThrowsResourceNotFoundException() {
        Long requestId = 1L;
        String newStatus = "IN_PROGRESS";

        when(maintenanceRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.updateStatus(requestId, newStatus));
        verify(maintenanceRepository, times(1)).findById(requestId);
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
        verify(mapper, never()).toDto(any(Maintenance.class));
    }

    @Test
    void updateStatus_InvalidStatus_ThrowsIllegalArgumentException() {
        Long requestId = 1L;
        String invalidStatus = "INVALID_STATUS";
        Maintenance maintenance = new Maintenance();
        maintenance.setRequestId(requestId);

        when(maintenanceRepository.findById(requestId)).thenReturn(Optional.of(maintenance));

        assertThrows(IllegalArgumentException.class, () -> maintenanceService.updateStatus(requestId, invalidStatus));
        verify(maintenanceRepository, times(1)).findById(requestId);
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
        verify(mapper, never()).toDto(any(Maintenance.class));
    }

    @Test
    void getAllByTenant_ExistingTenantId_ReturnsListOfMaintenanceDTO() {
        Long tenantId = 1L;
        User tenant = new User();
        tenant.setId(tenantId);
        Property property1 = new Property();
        Property property2 = new Property();
        Maintenance maintenance1 = new Maintenance();
        maintenance1.setRequestId(201L);
        maintenance1.setTenant(tenant);
        maintenance1.setProperty(property1);
        Maintenance maintenance2 = new Maintenance();
        maintenance2.setRequestId(202L);
        maintenance2.setTenant(tenant);
        maintenance2.setProperty(property2);
        List<Maintenance> maintenanceList = Arrays.asList(maintenance1, maintenance2);
        MaintenanceDTO dto1 = MaintenanceDTO.builder().requestId(201L).build();
        MaintenanceDTO dto2 = MaintenanceDTO.builder().requestId(202L).build();
        List<MaintenanceDTO> dtoList = Arrays.asList(dto1, dto2);

        when(maintenanceRepository.findByTenant_Id(tenantId)).thenReturn(maintenanceList);
        when(mapper.toDto(maintenance1)).thenReturn(dto1);
        when(mapper.toDto(maintenance2)).thenReturn(dto2);

        List<MaintenanceDTO> result = maintenanceService.getAllByTenant(tenantId);

        assertNotNull(result);
        assertEquals(maintenanceList.size(), result.size());
        verify(maintenanceRepository, times(1)).findByTenant_Id(tenantId);
        verify(mapper, times(maintenanceList.size())).toDto(any(Maintenance.class));
    }

    @Test
    void getAllByTenant_NoRequestsForTenant_ReturnsEmptyList() {
        Long tenantId = 1L;

        when(maintenanceRepository.findByTenant_Id(tenantId)).thenReturn(Collections.emptyList());

        List<MaintenanceDTO> result = maintenanceService.getAllByTenant(tenantId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(maintenanceRepository, times(1)).findByTenant_Id(tenantId);
        verify(mapper, never()).toDto(any(Maintenance.class));
    }
}