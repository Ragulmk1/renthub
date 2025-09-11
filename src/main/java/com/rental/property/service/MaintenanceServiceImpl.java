
package com.rental.property.service;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.entity.Maintenance;
import com.rental.property.entity.Property;
import com.rental.property.entity.User;
import com.rental.property.enums.MaintenanceRequestStatus;
import com.rental.property.repo.MaintenanceRepository;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.util.MaintenanceMapper;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


import com.rental.property.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Data
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final MaintenanceMapper mapper;



    @Override
    public MaintenanceRequestDTO createRequest(MaintenanceRequestDTO dto) {
        Long tenantId = dto.getTenantId();
        Long propertyId = dto.getPropertyId();


        User tenant = userRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant (User) not found with ID: " + tenantId));


        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + propertyId));

        Maintenance maintenance = new Maintenance();

        maintenance.setTenant(tenant);
        maintenance.setProperty(property);
        maintenance.setIssueType(dto.getIssueType());
        maintenance.setIssueDescription(dto.getIssueDescription());
        maintenance.setStatus(MaintenanceRequestStatus.OPEN);


        Maintenance saved = maintenanceRepository.save(maintenance);


        return mapper.toRequestDto(saved);
    }

    @Override
    public List<MaintenanceDTO> getAllByLandlord(Long landlordId) {

        return maintenanceRepository.findByProperty_User_Id(landlordId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public MaintenanceDTO updateStatus(Long requestId, String status) {
        Maintenance req = maintenanceRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance request not found with ID: " + requestId)); // Use custom exception
        try {
            req.setStatus(MaintenanceRequestStatus.valueOf(status.toUpperCase()));
            return mapper.toDto(maintenanceRepository.save(req));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status provided: " + status, e);
        }
    }

    @Override
    public List<MaintenanceDTO> getAllByTenant(Long tenantId) {

        return maintenanceRepository.findByTenant_Id(tenantId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }


}