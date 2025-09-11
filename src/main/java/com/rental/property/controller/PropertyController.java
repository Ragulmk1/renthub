package com.rental.property.controller;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionResponseDto;
import com.rental.property.exception.PropertyNotFoundException;
import com.rental.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/landlord/properties")
public class PropertyController {
    private final PropertyService propertyService;
    @PostMapping
    public ResponseEntity<PropertyResponseDto> addNewProperty(@Valid @RequestPart PropertyRequestDto propertyRequestDto,
                                                              @RequestPart MultipartFile image) throws IOException {
        log.info("Adding new property: {}", propertyRequestDto);
        return new ResponseEntity<>(propertyService.addNewProperty(propertyRequestDto, image), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<PropertyResponseDto>> getAllPropertyByLandLordId(@PathVariable Long id,
                                                                                Pageable pageable) {
        log.info("Fetching all properties for owner ID: {}", id);
        return new ResponseEntity<>(propertyService.getAllPropertyByLandLordId(id, pageable), HttpStatus.OK);
    }
    @GetMapping("/viewProperty/{propertyId}")
    public ResponseEntity<PropertyResponseDto> getPropertyById(@PathVariable Long propertyId) {
        log.info("Fetching property details for property ID: {}", propertyId);
        return new ResponseEntity<>(propertyService.getPropertyById(propertyId), HttpStatus.OK);
    }
    @PutMapping("/{propertyId}")
    public ResponseEntity<PropertyResponseDto> updateProperty(@PathVariable("propertyId") Long propertyId,
                                                              @Valid @RequestBody PropertyRequestDto propertyResponseDto) {
        log.info("Updating property ID: {} with details: {}", propertyId, propertyResponseDto);
        PropertyResponseDto propertyResponseDto1 = propertyService.updateProperty(propertyId, propertyResponseDto);
        return new ResponseEntity<>(propertyResponseDto1, HttpStatus.OK);
    }
    @DeleteMapping("/{propertyId}")
    public ResponseEntity<?> deleteProperty(@PathVariable("propertyId") Long propertyId) {
        log.info("Deleting property with ID: {}", propertyId);
        try {
            propertyService.deleteProperty(propertyId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (PropertyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PatchMapping("/updateStatus/{tenantId}/{status}")
    public ResponseEntity<?> acceptApplication(@PathVariable Long tenantId, @PathVariable String status) {
        propertyService.updateApplicationStatus(tenantId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/transactions/{propertyId}")
    public ResponseEntity<List<RentalTransactionResponseDto>> getAllTenantTransactions(@PathVariable Long propertyId) {
        List<RentalTransactionResponseDto> transactionList = propertyService.getAllTenantTransactions(propertyId);
        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
    @GetMapping("/transaction/{landlordId}")
    public ResponseEntity<List<RentalTransactionResponseDto>> getAllTenantTransactionsByLandLordId(@PathVariable Long landlordId){
        List<RentalTransactionResponseDto> transactionList=propertyService.getAllTenantTransactionsByLandLordId(landlordId);
        return new ResponseEntity<>(transactionList,HttpStatus.OK);
    }

}