package com.rental.property.service;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import com.rental.property.exception.PropertyNotFoundException;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.RentalTransactionRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.util.EntityMapper;
import com.rental.property.util.RentalTransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@RequiredArgsConstructor
@Service
public class RentalTransactionServiceImpl implements RentalTransactionService {
    private  final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private  final RentalTransactionRepository rentalTransactionRepository;
    private  final EntityMapper entityMapper;
    @Override
    public List<PropertyResponseDto> getAllProperty() {
        List<Property> propertyList=propertyRepository.findAll();
        return entityMapper.propListToPropResponseDtoList(propertyList);
    }
    @Override
    public List<PropertyResponseDto> searchProperties(String city, String minPrice, String maxPrice, String bhk) {
        List<Property> propertyList=propertyRepository.searchProperties(city,maxPrice,minPrice,bhk);
        return entityMapper.propListToPropResponseDtoList(propertyList);
    }
    @Override
    public RentalTransactionDto applyForProperty(Long propertyId, RentalTransactionDto dto, Long userId) {
        Property property=propertyRepository.findById(propertyId).orElseThrow(()->new PropertyNotFoundException("No " +
                "property for this id"));
        if(property.getAvailabilityStatus().equals("Available")) {
            RentalTransaction rentalTransactionEntity = RentalTransactionMapper.toEntity(dto);
            rentalTransactionEntity.setStatus("Applied");

            User user = userRepository.findById(userId).get();
            rentalTransactionEntity.setUser(user);
            rentalTransactionEntity.setProperty(property);
            RentalTransaction savedRentalTransaction = rentalTransactionRepository.save(rentalTransactionEntity);
            return RentalTransactionMapper.toDTO(savedRentalTransaction);
        }
        else {
            throw new PropertyNotFoundException("Already Rented");
        }
    }
    @Override
    public List<RentalTransactionTenantResponse> viewApplicationStatus(Long userId) {
        List<RentalTransaction> rentalTransactionList=rentalTransactionRepository.findAllByUser_Id(userId);
        return RentalTransactionMapper.convertToTenantResponseList(rentalTransactionList);
    }
}
