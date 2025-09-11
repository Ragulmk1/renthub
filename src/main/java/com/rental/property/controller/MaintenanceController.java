package com.rental.property.controller;

import com.rental.property.dto.MaintenanceDTO;
import com.rental.property.dto.MaintenanceRequestDTO;
import com.rental.property.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MaintenanceController {

    private final MaintenanceService service;

    @PostMapping("/tenant/raiseMaintenanceRequest")
    public ResponseEntity<MaintenanceRequestDTO> create(@RequestBody MaintenanceRequestDTO dto) {
        return ResponseEntity.ok(service.createRequest(dto));
    }

    @GetMapping("/landlord/viewAllMaintenaceReq/{landlordId}")
    public ResponseEntity<List<MaintenanceDTO>> getByLandlord(@PathVariable Long landlordId) {
        return ResponseEntity.ok(service.getAllByLandlord(landlordId));
    }


    @PatchMapping("/landlord/updateMaintenanceReqStatus/{requestId}/status")
    public ResponseEntity<MaintenanceDTO> updateStatus(@PathVariable Long requestId, @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(requestId, status));
    }

    @GetMapping("/tenant/viewAllMaintenanceReq/{tenantId}")
    public ResponseEntity<List<MaintenanceDTO>> getByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(service.getAllByTenant(tenantId));
    }

}