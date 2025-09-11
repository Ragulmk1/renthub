
package com.rental.property.service;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface MaintenanceService {
    MaintenanceRequestDTO createRequest(MaintenanceRequestDTO dto);

    List<MaintenanceDTO> getAllByLandlord(Long landlordId);
    MaintenanceDTO updateStatus(Long requestId, String status);
    List<MaintenanceDTO> getAllByTenant(Long tenantId);
}