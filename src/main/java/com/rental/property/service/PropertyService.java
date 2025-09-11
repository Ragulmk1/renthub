package com.rental.property.service;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
@Service
public interface PropertyService {
    PropertyResponseDto addNewProperty(PropertyRequestDto propertyRequestDto, MultipartFile image) throws IOException;
    PropertyResponseDto getPropertyById(Long propertyId);
    PropertyResponseDto updateProperty(Long propertyId, PropertyRequestDto propertyRequestDto);
    void deleteProperty(Long propertyId);
    List<PropertyResponseDto> getAllPropertyByLandLordId(Long ownerId, Pageable pageable);
    void updateApplicationStatus(Long tenantId, String status);
    List<RentalTransactionResponseDto> getAllTenantTransactions(Long propertyId);
 List<RentalTransactionResponseDto> getAllTenantTransactionsByLandLordId(Long landlordId);

    List<PropertyResponseDto> getAllPropertyById(Long id);


}

