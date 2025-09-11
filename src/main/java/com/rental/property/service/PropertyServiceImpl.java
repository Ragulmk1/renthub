package com.rental.property.service;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionResponseDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service

public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final EntityMapper entityMapper;
    private final UserRepository userRepository;
    private final RentalTransactionRepository rentalTransactionRepository;
    private final RentalTransactionMapper rentalTransactionMapper;

    @Override
    public PropertyResponseDto addNewProperty(PropertyRequestDto propertyRequestDto, MultipartFile image) throws IOException {
        Property propObj = entityMapper.convertPropRequestDtoToProperty(propertyRequestDto);
// propObj.setLandlordId(propObj.getUser().getId());
        Set<String> roles =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        if (roles.contains("ROLE_LANDLORD")) {
            Long currentLandLordId = getCurrentLandLordId();
            User landlord = userRepository.findById(currentLandLordId).get();
            propObj.setUser(landlord);
            propObj.setImage1(image.getBytes());
            propObj.setLandlordId(currentLandLordId);
            propObj.setCreatedBy(landlord.getUsername());
            propObj.setCreatedDate(LocalDateTime.now());
            Property savedProp = propertyRepository.save(propObj);
            return entityMapper.convertPropToPropResponseDto(savedProp);
        } else {
            throw new RuntimeException("Sign in as landlord");
        }
    }

    private Long getCurrentLandLordId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user =
                    userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Authenticated User " +
                            "Not Found in DataBase"));
            return user.getId();
        } else {
            throw new RuntimeException("Unable to retrieve current landlord ID: User not authenticated.");
        }
    }

    @Override
    public PropertyResponseDto getPropertyById(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new PropertyNotFoundException("No " +
                "such " +
                "property"));
        return entityMapper.convertPropToPropResponseDto(property);
    }

    @Override
    public PropertyResponseDto updateProperty(Long propertyId, PropertyRequestDto propertyRequestDto) {
        Property existing = propertyRepository.findById(propertyId).orElseThrow(() -> new PropertyNotFoundException("No such property"));
        if (existing.getUser().getId() == getCurrentLandLordId()) {
            existing.setAddress(propertyRequestDto.getAddress());
            existing.setDescription(propertyRequestDto.getDescription());
            existing.setAvailabilityStatus(propertyRequestDto.getAvailabilityStatus());
            existing.setRentAmount(propertyRequestDto.getRentAmount());
            existing.setPropertyType(propertyRequestDto.getPropertyType());
            existing.setUpdatedBy(existing.getUser().getUsername());
            existing.setUpdatedDate(LocalDateTime.now());
            existing.setLandlordId(existing.getLandlordId());
            propertyRepository.save(existing);
            return entityMapper.convertPropToPropResponseDto(existing);
        } else {
            throw new AuthorizationDeniedException("This property does not belongs to you");
        }
    }

    @Override
    public void deleteProperty(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new PropertyNotFoundException(
                "property does not exist"));
        if (property.getUser().getId() == getCurrentLandLordId()) {
            propertyRepository.deleteById(propertyId);
        } else {
            throw new AuthorizationDeniedException("This property does not belongs to you");
        }
    }

    @Override
    public List<PropertyResponseDto> getAllPropertyByLandLordId(Long id, Pageable pageable) {
        Page<Property> propertyPage = propertyRepository.findPropertyByOwnerId(id, pageable);
        List<Property> propertyList = propertyPage.getContent();
        log.info("Total records fetched from DB = {} total pages = {} records in each page {} is {} ",
                propertyPage.getTotalElements(), propertyPage.getTotalPages(), propertyPage.getNumber(), propertyList.size());
        return entityMapper.propListToPropResponseDtoList(propertyList);
    }

    @Override
    public void updateApplicationStatus(Long tenantId, String status) {
        RentalTransaction rentalTransaction = rentalTransactionRepository.findById(tenantId).get();
//        Long landLordId = rentalTransaction.getProperty().getUser().getId();
//        if (getCurrentLandLordId() == landLordId) {
            rentalTransaction.setStatus(status);
            rentalTransactionRepository.save(rentalTransaction);
//        } else {
//            throw new AuthorizationDeniedException("This property does not belongs to you");
//        }
    }

    @Override
    public List<RentalTransactionResponseDto> getAllTenantTransactions(Long propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        List<RentalTransaction> rentalTransactionList = rentalTransactionRepository.findByProperty(property);
        return RentalTransactionMapper.convertToResponseDtoList(rentalTransactionList);
    }

//    @Override
//    public List<RentalTransactionResponseDto> getAllTenantTransactionsByLandLordId(Long landlordId) {
//        List<RentalTransaction> rentalTransactionList=rentalTransactionRepository.findByLandLordId(landlordId);
//        return RentalTransactionMapper.convertToResponseDtoList(rentalTransactionList);
//    }
//    @Override
//    public byte[] getImage(Long propertyId) {
//        return propertyRepository.findById(propertyId).get().getImage1();
//    }

    @Override
    public List<RentalTransactionResponseDto> getAllTenantTransactionsByLandLordId(Long landlordId) {
        List<RentalTransaction> rentalTransactionList=rentalTransactionRepository.findByLandLordId(landlordId);
        return RentalTransactionMapper.convertToResponseDtoList(rentalTransactionList);
    }

    @Override
    public List<PropertyResponseDto> getAllPropertyById(Long id) {

        User tenant = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + id));
        List<RentalTransaction> rentedTransactions = rentalTransactionRepository.findAllByUser_Id(id);
        log.info("Rented transactions {}",rentedTransactions);
        List<RentalTransaction> rentalTransactionList=rentedTransactions.stream().filter(r->r.getStatus().equals(
                "Completed")).collect(Collectors.toList());
        List<Property> rentedProperties = rentalTransactionList.stream()
                .map(RentalTransaction::getProperty).filter(p->p.getAvailabilityStatus().equals("Rented"))
                .collect(Collectors.toList());
        return entityMapper.propListToPropResponseDtoList(rentedProperties);
    }
}