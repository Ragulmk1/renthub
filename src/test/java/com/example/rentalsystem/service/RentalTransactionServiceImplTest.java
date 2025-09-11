package com.example.rentalsystem.service;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Address;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.RentalTransactionRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.RentalTransactionServiceImpl;
import com.rental.property.util.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class RentalTransactionServiceImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalTransactionRepository rentalTransactionRepository;
    @Mock
    private EntityMapper entityMapper;
    // Note: RentalTransactionMapper has static methods, so mocking it directly might not be the best approach.
    // We'll need to handle its behavior differently or consider making it a Spring Bean for easier mocking.
    @InjectMocks
    private RentalTransactionServiceImpl rentalTransactionService;
    private Property property;
    private User user;
    private RentalTransaction rentalTransaction;
    private RentalTransactionDto rentalTransactionDto;
    private PropertyResponseDto propertyResponseDto;
    private RentalTransactionTenantResponse tenantResponse;
    @BeforeEach
    void setUp() {
        // Initialize entities and DTOs for testing
        Address addressEntity = Address.builder()
                .streetName("Street 1")
                .city("Chennai")
                .state("TN")
                .pinCode(600001L)
                .build();
        property = Property.builder()
                .propertyId(101L)
                .address(addressEntity)
                .bhk("3BHK")
                .description("Nice property")
                .build();
        user = User.builder()
                .username("tenant1")
                .mobileNo(9876543210L)
                .build();
        rentalTransaction = RentalTransaction.builder()
                .transactionId(301L)
                .property(property)
                .user(user)
                .status("Applied")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(12))
                .build();
        rentalTransactionDto = RentalTransactionDto.builder()
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(12))
                .build();
        propertyResponseDto = PropertyResponseDto.builder()
                .propertyId(101L)
                .address(addressEntity) // Corrected line: Passing the Address entity
                .bhk("3BHK")
                .build();
        tenantResponse = RentalTransactionTenantResponse.builder()
                .propertyId(101L)
                .address("Street 1, Chennai, TN 600001")
                .bhk("3BHK")
                .description("Nice property")
                .status("Applied")
                .build();
    }
    @Test
    void getAllProperty_shouldReturnListOfPropertyResponseDtos() {
        List<Property> propertyList = List.of(property);
        List<PropertyResponseDto> responseList = List.of(propertyResponseDto);
        when(propertyRepository.findAll()).thenReturn(propertyList);
        when(entityMapper.propListToPropResponseDtoList(propertyList)).thenReturn(responseList);
        List<PropertyResponseDto> result = rentalTransactionService.getAllProperty();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(propertyResponseDto.getPropertyId(), result.get(0).getPropertyId());
        verify(propertyRepository, times(1)).findAll();
        verify(entityMapper, times(1)).propListToPropResponseDtoList(propertyList);
    }
    @Test
    void searchProperties_shouldReturnListOfPropertyResponseDtos() {
        List<Property> propertyList = List.of(property);
        List<PropertyResponseDto> responseList = List.of(propertyResponseDto);
        String city = "Chennai";
        String minPrice = "10000";
        String maxPrice = "20000";
        String bhk = "3BHK";
        when(propertyRepository.searchProperties(city, maxPrice, minPrice, bhk)).thenReturn(propertyList);
        when(entityMapper.propListToPropResponseDtoList(propertyList)).thenReturn(responseList);
        List<PropertyResponseDto> result = rentalTransactionService.searchProperties(city, minPrice, maxPrice, bhk);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(propertyResponseDto.getPropertyId(), result.get(0).getPropertyId());
        verify(propertyRepository, times(1)).searchProperties(city, maxPrice, minPrice, bhk);
        verify(entityMapper, times(1)).propListToPropResponseDtoList(propertyList);
    }

}
