package com.rental.property.util;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.entity.Address;
import com.rental.property.entity.Property;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class EntityMapper {
    public Property convertPropRequestDtoToProperty(PropertyRequestDto propertyRequestDto) {
        Address address = new Address(propertyRequestDto.getAddress().getStreetName(), propertyRequestDto.getAddress().getCity(),
                propertyRequestDto.getAddress().getState(), propertyRequestDto.getAddress().getPinCode());
        return Property.builder()
                .address(address)
                .propertyType(propertyRequestDto.getPropertyType())
                .bhk(propertyRequestDto.getBhk())
                .rentAmount(propertyRequestDto.getRentAmount())
                .availabilityStatus(propertyRequestDto.getAvailabilityStatus())
                .description(propertyRequestDto.getDescription())
                .build();
    }
    public PropertyResponseDto convertPropToPropResponseDto(Property property) {
        Address address = new Address(property.getAddress().getStreetName(), property.getAddress().getCity(),
                property.getAddress().getState(), property.getAddress().getPinCode());
        return PropertyResponseDto.builder()
                .propertyId(property.getPropertyId())
                .landLordId(property.getLandlordId())
                .address(address)
                .propertyType(property.getPropertyType())
                .bhk(property.getBhk())
                .rentAmount(property.getRentAmount())
                .availabilityStatus(property.getAvailabilityStatus())
                .description(property.getDescription())
                .image1(property.getImage1())
                .build();
    }
    public List<PropertyResponseDto> propListToPropResponseDtoList(List<Property> properties) {
        List<PropertyResponseDto> propertyResponseDtoList=
                properties.stream().map(p->convertPropToPropResponseDto(p)).collect(Collectors.toList());
        return propertyResponseDtoList;
    }
    public List<Property> propRequestDtoListToPropList(List<PropertyRequestDto> propertyRequestDtoList){
        List<Property> propertyList=
                propertyRequestDtoList.stream().map(p->convertPropRequestDtoToProperty(p)).collect(Collectors.toList());
        return propertyList;
    }
}
