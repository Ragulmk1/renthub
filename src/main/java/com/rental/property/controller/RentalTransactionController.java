package com.rental.property.controller;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Property;
import com.rental.property.service.PropertyService;
import com.rental.property.service.RentalTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenant")
public class RentalTransactionController {
    private final RentalTransactionService rentalTransactionService;
    private final PropertyService propertyService;
    @GetMapping("/viewProperties/")
    public List<PropertyResponseDto> getAllProperty(){
        log.info("Fetching all properties");
        return rentalTransactionService.getAllProperty();
    }
    @GetMapping("/search")
    public List<PropertyResponseDto> searchProperties(
            @RequestParam(required=false) String city,
            @RequestParam(required=false) String minPrice,
            @RequestParam(required=false) String maxPrice,
            @RequestParam(required=false) String bhk){
        log.info("Searching properties with city: {}, minPrice: {}, maxPrice: {}, bhk: {}", city, minPrice, maxPrice, bhk);
        return rentalTransactionService.searchProperties(city,minPrice,maxPrice,bhk);
    }

    @GetMapping("/viewProperty/{propertyId}")
    public ResponseEntity<PropertyResponseDto> getPropertyById(@PathVariable Long propertyId) {
        log.info("Fetching property details for property ID: {}", propertyId);
        return new ResponseEntity<>(propertyService.getPropertyById(propertyId), HttpStatus.OK);
    }
    @PostMapping("/{userId}/{property_id}/apply")
    public RentalTransactionDto applyForProperty(
            @PathVariable("property_id") Long propertyId,
            @RequestBody RentalTransactionDto rentalTransactionDto,@PathVariable("userId") Long userId) {
        return rentalTransactionService.applyForProperty(propertyId, rentalTransactionDto,userId);
    }
    @GetMapping("/ApplicationStatus/{userId}")
    public ResponseEntity<List<RentalTransactionTenantResponse>> viewApplicationStatus(@PathVariable Long userId){
        List<RentalTransactionTenantResponse> rentalTransactionTenantResponseList=
                rentalTransactionService.viewApplicationStatus(userId);
        return new  ResponseEntity<>(rentalTransactionTenantResponseList,HttpStatus.OK);
    }

    @GetMapping("/MyRentals/{id}")
    public ResponseEntity<List<PropertyResponseDto>> getAllPropertyById(@PathVariable Long id
                                                                               ) {
        log.info("Fetching all properties for owner ID: {}", id);
        return new ResponseEntity<>(propertyService.getAllPropertyById(id), HttpStatus.OK);
    }

}
