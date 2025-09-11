package com.example.rentalsystem.util;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.entity.Address;
import com.rental.property.entity.Property;
import com.rental.property.util.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(MockitoExtension.class)
public class EntityMapperTest {
    @InjectMocks
    private EntityMapper entityMapper;
    @Test
    void convertPropRequestDtoToProperty_Success() {
        PropertyRequestDto propertyRequestDto = PropertyRequestDto.builder()
                .address(Address.builder()
                        .streetName("123 Main St")
                        .city("Chennai")
                        .state("TN")
                        .pinCode(600001L)
                        .build())
                .propertyType("Apartment")
                .bhk("2BHK")
                .rentAmount(15000.0)
                .availabilityStatus("Available")
                .description("Cozy 2BHK apartment")
                .build();
        Property property = entityMapper.convertPropRequestDtoToProperty(propertyRequestDto);
        assertNotNull(property);
        assertEquals(propertyRequestDto.getPropertyType(), property.getPropertyType());
        assertEquals(propertyRequestDto.getBhk(), property.getBhk());
        assertEquals(propertyRequestDto.getRentAmount(), property.getRentAmount());
        assertEquals(propertyRequestDto.getAvailabilityStatus(), property.getAvailabilityStatus());
        assertEquals(propertyRequestDto.getDescription(), property.getDescription());
        assertEquals(propertyRequestDto.getAddress().getStreetName(), property.getAddress().getStreetName());
        assertEquals(propertyRequestDto.getAddress().getCity(), property.getAddress().getCity());
        assertEquals(propertyRequestDto.getAddress().getState(), property.getAddress().getState());
        assertEquals(propertyRequestDto.getAddress().getPinCode(), property.getAddress().getPinCode());
    }
    @Test
    void convertPropToPropResponseDto_Success() {
        Property property = Property.builder()
                .propertyId(1L)
                .landlordId(101L)
                .address(Address.builder()
                        .streetName("456 Park Ave")
                        .city("Bangalore")
                        .state("KA")
                        .pinCode(560002L)
                        .build())
                .propertyType("Villa")
                .bhk("4BHK")
                .rentAmount(30000.0)
                .availabilityStatus("Available")
                .description("Spacious 4BHK villa with garden")
                .image1(new byte[]{1, 2, 3})
                .build();
        PropertyResponseDto propertyResponseDto = entityMapper.convertPropToPropResponseDto(property);
        assertNotNull(propertyResponseDto);
        assertEquals(property.getPropertyId(), propertyResponseDto.getPropertyId());
        assertEquals(property.getLandlordId(), propertyResponseDto.getLandLordId());
        assertEquals(property.getPropertyType(), propertyResponseDto.getPropertyType());
        assertEquals(property.getBhk(), propertyResponseDto.getBhk());
        assertEquals(property.getRentAmount(), propertyResponseDto.getRentAmount());
        assertEquals(property.getAvailabilityStatus(), propertyResponseDto.getAvailabilityStatus());
        assertEquals(property.getDescription(), propertyResponseDto.getDescription());
        assertEquals(property.getImage1(), propertyResponseDto.getImage1());
        assertEquals(property.getAddress().getStreetName(), propertyResponseDto.getAddress().getStreetName());
        assertEquals(property.getAddress().getCity(), propertyResponseDto.getAddress().getCity());
        assertEquals(property.getAddress().getState(), propertyResponseDto.getAddress().getState());
        assertEquals(property.getAddress().getPinCode(), propertyResponseDto.getAddress().getPinCode());
    }
    @Test
    void propListToPropResponseDtoList_Success() {
        Property property1 = Property.builder()
                .propertyId(1L)
                .landlordId(101L)
                .address(Address.builder()
                        .streetName("123 Main St")
                        .city("Chennai")
                        .state("TN")
                        .pinCode(600001L)
                        .build())
                .propertyType("Apartment")
                .bhk("2BHK")
                .rentAmount(15000.0)
                .availabilityStatus("Available")
                .description("Cozy 2BHK apartment")
                .image1(new byte[]{1, 2, 3})
                .build();
        Property property2 = Property.builder()
                .propertyId(2L)
                .landlordId(102L)
                .address(Address.builder()
                        .streetName("456 Park Ave")
                        .city("Bangalore")
                        .state("KA")
                        .pinCode(560002L)
                        .build())
                .propertyType("Villa")
                .bhk("4BHK")
                .rentAmount(30000.0)
                .availabilityStatus("Available")
                .description("Spacious 4BHK villa with garden")
                .image1(new byte[]{4, 5, 6})
                .build();
        List<Property> propertyList = Arrays.asList(property1, property2);
        List<PropertyResponseDto> responseDtoList = entityMapper.propListToPropResponseDtoList(propertyList);
        assertNotNull(responseDtoList);
        assertEquals(propertyList.size(), responseDtoList.size());
        PropertyResponseDto responseDto1 = responseDtoList.get(0);
        assertEquals(property1.getPropertyId(), responseDto1.getPropertyId());
        assertEquals(property1.getLandlordId(), responseDto1.getLandLordId());
        assertEquals(property1.getPropertyType(), responseDto1.getPropertyType());
        assertEquals(property1.getBhk(), responseDto1.getBhk());
        assertEquals(property1.getAddress().getStreetName(), responseDto1.getAddress().getStreetName());
        assertEquals(property1.getAddress().getCity(), responseDto1.getAddress().getCity());
        assertEquals(property1.getAddress().getState(), responseDto1.getAddress().getState());
        assertEquals(property1.getAddress().getPinCode(), responseDto1.getAddress().getPinCode());
        PropertyResponseDto responseDto2 = responseDtoList.get(1);
        assertEquals(property2.getPropertyId(), responseDto2.getPropertyId());
        assertEquals(property2.getLandlordId(), responseDto2.getLandLordId());
        assertEquals(property2.getPropertyType(), responseDto2.getPropertyType());
        assertEquals(property2.getBhk(), responseDto2.getBhk());
        assertEquals(property2.getAddress().getStreetName(), responseDto2.getAddress().getStreetName());
        assertEquals(property2.getAddress().getCity(), responseDto2.getAddress().getCity());
        assertEquals(property2.getAddress().getState(), responseDto2.getAddress().getState());
        assertEquals(property2.getAddress().getPinCode(), responseDto2.getAddress().getPinCode());
    }
    @Test
    void propRequestDtoListToPropList_Success() {
        PropertyRequestDto propertyRequestDto1 = PropertyRequestDto.builder()
                .address(Address.builder()
                        .streetName("123 Main St")
                        .city("Chennai")
                        .state("TN")
                        .pinCode(600001L)
                        .build())
                .propertyType("Apartment")
                .bhk("2BHK")
                .rentAmount(15000.0)
                .availabilityStatus("Available")
                .description("Cozy 2BHK apartment")
                .build();
        PropertyRequestDto propertyRequestDto2 = PropertyRequestDto.builder()
                .address(Address.builder()
                        .streetName("456 Park Ave")
                        .city("Bangalore")
                        .state("KA")
                        .pinCode(560002L)
                        .build())
                .propertyType("Villa")
                .bhk("4BHK")
                .rentAmount(30000.0)
                .availabilityStatus("Available")
                .description("Spacious 4BHK villa with garden")
                .build();
        List<PropertyRequestDto> requestDtoList = Arrays.asList(propertyRequestDto1, propertyRequestDto2);
        List<Property> propertyList = entityMapper.propRequestDtoListToPropList(requestDtoList);
        assertNotNull(propertyList);
        assertEquals(requestDtoList.size(), propertyList.size());
        Property property1 = propertyList.get(0);
        assertEquals(requestDtoList.get(0).getPropertyType(), property1.getPropertyType());
        assertEquals(requestDtoList.get(0).getBhk(), property1.getBhk());
        assertEquals(requestDtoList.get(0).getAddress().getPinCode(), property1.getAddress().getPinCode());
        assertEquals(requestDtoList.get(0).getAddress().getStreetName(), property1.getAddress().getStreetName());
        assertEquals(requestDtoList.get(0).getAddress().getCity(), property1.getAddress().getCity());
        assertEquals(requestDtoList.get(0).getAddress().getState(), property1.getAddress().getState());
        Property property2 = propertyList.get(1);
        assertEquals(requestDtoList.get(1).getPropertyType(), property2.getPropertyType());
        assertEquals(requestDtoList.get(1).getBhk(), property2.getBhk());
        assertEquals(requestDtoList.get(1).getAddress().getPinCode(), property2.getAddress().getPinCode());
        assertEquals(requestDtoList.get(1).getAddress().getStreetName(), property2.getAddress().getStreetName());
        assertEquals(requestDtoList.get(1).getAddress().getCity(), property2.getAddress().getCity());
        assertEquals(requestDtoList.get(1).getAddress().getState(), property2.getAddress().getState());
    }
}