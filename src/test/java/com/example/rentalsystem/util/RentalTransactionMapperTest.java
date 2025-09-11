package com.example.rentalsystem.util;

import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionResponseDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Address;
import com.rental.property.entity.Property;
import com.rental.property.entity.RentalTransaction;
import com.rental.property.entity.User;
import com.rental.property.util.RentalTransactionMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RentalTransactionMapperTest {

    @Test
    void toDTO_shouldMapEntityToDTO() {
        RentalTransaction entity = RentalTransaction.builder()
                .startDate(LocalDate.of(2025, 6, 10))
                .endDate(LocalDate.of(2026, 6, 9))
                .build();

        RentalTransactionDto dto = RentalTransactionMapper.toDTO(entity);

        assertEquals(entity.getStartDate(), dto.getStartDate());
        assertEquals(entity.getEndDate(), dto.getEndDate());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        RentalTransactionDto dto = RentalTransactionDto.builder()
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2026, 6, 30))
                .build();

        RentalTransaction entity = RentalTransactionMapper.toEntity(dto);

        assertEquals(dto.getStartDate(), entity.getStartDate());
        assertEquals(dto.getEndDate(), entity.getEndDate());
        assertNotNull(entity.getProperty()); // Property is created as new in toEntity
    }

    @Test
    void tenantListToTenantDtoList_shouldMapListOfEntitiesToListOfDTOs() {
        List<RentalTransaction> entityList = Arrays.asList(
                RentalTransaction.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build(),
                RentalTransaction.builder().startDate(LocalDate.now().plusDays(5)).endDate(LocalDate.now().plusMonths(2)).build()
        );

        List<RentalTransactionDto> dtoList = RentalTransactionMapper.tenantListToTenantDtoList(entityList);

        assertEquals(entityList.size(), dtoList.size());
        assertEquals(entityList.get(0).getStartDate(), dtoList.get(0).getStartDate());
        assertEquals(entityList.get(0).getEndDate(), dtoList.get(0).getEndDate());
        assertEquals(entityList.get(1).getStartDate(), dtoList.get(1).getStartDate());
        assertEquals(entityList.get(1).getEndDate(), dtoList.get(1).getEndDate());
    }

    @Test
    void tenantDtoListToTenantList_shouldMapListOfDTOsToListOfEntities() {
        List<RentalTransactionDto> dtoList = Arrays.asList(
                RentalTransactionDto.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).build(),
                RentalTransactionDto.builder().startDate(LocalDate.now().plusDays(5)).endDate(LocalDate.now().plusMonths(2)).build()
        );

        List<RentalTransaction> entityList = RentalTransactionMapper.tenantDtoListToTenantList(dtoList);

        assertEquals(dtoList.size(), entityList.size());
        assertEquals(dtoList.get(0).getStartDate(), entityList.get(0).getStartDate());
        assertEquals(dtoList.get(0).getEndDate(), entityList.get(0).getEndDate());
        assertNotNull(entityList.get(0).getProperty());
        assertEquals(dtoList.get(1).getStartDate(), entityList.get(1).getStartDate());
        assertEquals(dtoList.get(1).getEndDate(), entityList.get(1).getEndDate());
        assertNotNull(entityList.get(1).getProperty());
    }

    @Test
    void convertToResponseDtoList_shouldMapListOfEntitiesToListOfResponseDTOs() {
        Address address = Address.builder().streetName("Street").city("City").state("State").pinCode(123L).build();
        Property property1 = Property.builder().propertyId(1L).address(address).bhk("2BHK").image1("image1.jpg".getBytes()).build();
        User user1 = User.builder().username("user1").mobileNo(9876543210L).build();
        RentalTransaction entity1 = RentalTransaction.builder().transactionId(101L).property(property1).startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(1)).user(user1).build();
        List<RentalTransaction> entityList = Arrays.asList(entity1);

        List<RentalTransactionResponseDto> responseDtoList = RentalTransactionMapper.convertToResponseDtoList(entityList);

        assertEquals(entityList.size(), responseDtoList.size());
        assertEquals(entity1.getTransactionId(), responseDtoList.get(0).getTransactionId());
        assertEquals(entity1.getProperty().getPropertyId(), responseDtoList.get(0).getPropertyId());
        assertEquals("Street, City, State 123", responseDtoList.get(0).getAddress());
        assertEquals(entity1.getProperty().getBhk(), responseDtoList.get(0).getBhk());
        assertEquals(entity1.getStatus(), responseDtoList.get(0).getStatus());
        assertEquals(entity1.getStartDate(), responseDtoList.get(0).getStartDate());
        assertEquals(entity1.getEndDate(), responseDtoList.get(0).getEndDate());
        assertEquals(entity1.getProperty().getImage1(), responseDtoList.get(0).getImage());
        assertEquals(entity1.getUser().getUsername(), responseDtoList.get(0).getUserName());
        assertEquals(String.valueOf(entity1.getUser().getMobileNo()), responseDtoList.get(0).getMobileNo());
    }

    @Test
    void convertToResponseDto_shouldMapEntityToResponseDTO() {
        Address address = Address.builder().streetName("Another Street").city("Another City").state("Another State").pinCode(456L).build();
        Property property = Property.builder().propertyId(2L).address(address).bhk("3BHK").image1("image2.png".getBytes()).build();
        User user = User.builder().username("testuser").mobileNo(1234567890L).build();
        RentalTransaction entity = RentalTransaction.builder().transactionId(202L).property(property).startDate(LocalDate.now().plusDays(7)).endDate(LocalDate.now().plusMonths(3)).status("Approved").user(user).build();

        RentalTransactionResponseDto responseDto = RentalTransactionMapper.convertToResponseDto(entity);

        assertEquals(entity.getTransactionId(), responseDto.getTransactionId());
        assertEquals(entity.getProperty().getPropertyId(), responseDto.getPropertyId());
        assertEquals("Another Street, Another City, Another State 456", responseDto.getAddress());
        assertEquals(entity.getProperty().getBhk(), responseDto.getBhk());
        assertEquals(entity.getStatus(), responseDto.getStatus());
        assertEquals(entity.getStartDate(), responseDto.getStartDate());
        assertEquals(entity.getEndDate(), responseDto.getEndDate());
        assertEquals(entity.getProperty().getImage1(), responseDto.getImage());
        assertEquals(entity.getUser().getUsername(), responseDto.getUserName());
        assertEquals(String.valueOf(entity.getUser().getMobileNo()), responseDto.getMobileNo());
    }

    @Test
    void convertToTenantResponseList_shouldMapListOfEntitiesToListOfTenantResponseDTOs() {
        Address address = Address.builder().streetName("Tenant Street").city("Tenant City").state("Tenant State").pinCode(789L).build();
        Property property1 = Property.builder().propertyId(3L).address(address).bhk("1BHK").description("Cozy 1BHK").image1("tenant_image1.jpg".getBytes()).build();
        RentalTransaction entity1 = RentalTransaction.builder().transactionId(303L).property(property1).status("Applied").build();
        List<RentalTransaction> entityList = Arrays.asList(entity1);

        List<RentalTransactionTenantResponse> responseDtoList = RentalTransactionMapper.convertToTenantResponseList(entityList);

        assertEquals(entityList.size(), responseDtoList.size());
        assertEquals(entity1.getProperty().getPropertyId(), responseDtoList.get(0).getPropertyId());
        assertEquals(entity1.getTransactionId(), responseDtoList.get(0).getTransactionId());
        assertEquals("Tenant Street, Tenant City, Tenant State 789", responseDtoList.get(0).getAddress());
        assertEquals(entity1.getProperty().getBhk(), responseDtoList.get(0).getBhk());
        assertEquals(entity1.getProperty().getDescription(), responseDtoList.get(0).getDescription());
        assertEquals(entity1.getStatus(), responseDtoList.get(0).getStatus());
        assertEquals(entity1.getProperty().getImage1(), responseDtoList.get(0).getImage());
    }

    @Test
    void convertToTenantResponse_shouldMapEntityToTenantResponseDTO() {
        Address address = Address.builder().streetName("Another Tenant Street").city("Another Tenant City").state("Another Tenant State").pinCode(012L).build();
        Property property = Property.builder().propertyId(4L).address(address).bhk("4BHK").description("Spacious 4BHK").image1("tenant_image2.png".getBytes()).build();
        RentalTransaction entity = RentalTransaction.builder().transactionId(404L).property(property).status("Approved").build();

        RentalTransactionTenantResponse responseDto = RentalTransactionMapper.convertToTenantResponse(entity);

        assertEquals(entity.getProperty().getPropertyId(), responseDto.getPropertyId());
        assertEquals(entity.getTransactionId(), responseDto.getTransactionId());
        assertEquals("Another Tenant Street, Another Tenant City, Another Tenant State 10", responseDto.getAddress());
        assertEquals(entity.getProperty().getBhk(), responseDto.getBhk());
        assertEquals(entity.getProperty().getDescription(), responseDto.getDescription());
        assertEquals(entity.getStatus(), responseDto.getStatus());
        assertEquals(entity.getProperty().getImage1(), responseDto.getImage());
    }
}