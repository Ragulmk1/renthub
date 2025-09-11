package com.rental.property.service;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public interface RentalTransactionService {
    List<PropertyResponseDto> getAllProperty();
    List<PropertyResponseDto> searchProperties(String city, String minPrice, String maxPrice, String bhk);
    RentalTransactionDto applyForProperty(Long propertyId, RentalTransactionDto rentalTransactionDto, Long userId);
    List<RentalTransactionTenantResponse> viewApplicationStatus(Long userId);
}
