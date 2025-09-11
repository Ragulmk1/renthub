package com.example.rentalsystem.controller;

import com.rental.property.controller.RentalTransactionController;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionDto;
import com.rental.property.dto.RentalTransactionTenantResponse;
import com.rental.property.entity.Address;
import com.rental.property.service.PropertyService;
import com.rental.property.service.RentalTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RentalTransactionControllerTest {

    @InjectMocks
    private RentalTransactionController rentalTransactionController;

    @Mock
    private RentalTransactionService rentalTransactionService;

    @Mock
    private PropertyService propertyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProperty_shouldReturnListOfPropertyResponseDto() {
        // Arrange
        List<PropertyResponseDto> mockProperties = Arrays.asList(
                PropertyResponseDto.builder().propertyId(1L).address(new Address()).build(),
                PropertyResponseDto.builder().propertyId(2L).address(new Address()).build()
        );
        when(rentalTransactionService.getAllProperty()).thenReturn(mockProperties);

        // Act
        List<PropertyResponseDto> result = rentalTransactionController.getAllProperty();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getPropertyId());
        assertEquals(2L, result.get(1).getPropertyId());
        verify(rentalTransactionService, times(1)).getAllProperty();
    }

    @Test
    void searchProperties_shouldReturnListOfPropertyResponseDtoBasedOnCriteria() {
        // Arrange
        List<PropertyResponseDto> mockSearchResults = Arrays.asList(
                PropertyResponseDto.builder().propertyId(3L).address(Address.builder().city("Chennai").build()).bhk("2BHK").build()
        );
        when(rentalTransactionService.searchProperties("Chennai", "10000", "20000", "2BHK")).thenReturn(mockSearchResults);

        // Act
        List<PropertyResponseDto> result = rentalTransactionController.searchProperties("Chennai", "10000", "20000", "2BHK");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Chennai", result.get(0).getAddress().getCity());
        assertEquals("2BHK", result.get(0).getBhk());
        verify(rentalTransactionService, times(1)).searchProperties("Chennai", "10000", "20000", "2BHK");
    }

    @Test
    void getPropertyById_shouldReturnResponseEntityWithPropertyResponseDtoAndStatusOK() {
        // Arrange
        Long propertyId = 4L;
        PropertyResponseDto mockProperty = PropertyResponseDto.builder().propertyId(propertyId).address(new Address()).build();
        when(propertyService.getPropertyById(propertyId)).thenReturn(mockProperty);

        // Act
        ResponseEntity<PropertyResponseDto> result = rentalTransactionController.getPropertyById(propertyId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(propertyId, result.getBody().getPropertyId());
        verify(propertyService, times(1)).getPropertyById(propertyId);
    }

    @Test
    void applyForProperty_shouldReturnRentalTransactionDtoFromService() {
        // Arrange
        Long propertyId = 5L;
        Long userId = 10L;
        RentalTransactionDto inputDto = RentalTransactionDto.builder().startDate(LocalDate.now()).endDate(LocalDate.now().plusMonths(12)).build();
        RentalTransactionDto mockResponseDto = RentalTransactionDto.builder().startDate(inputDto.getStartDate()).endDate(inputDto.getEndDate()).build();
        when(rentalTransactionService.applyForProperty(propertyId, inputDto, userId)).thenReturn(mockResponseDto);

        // Act
        RentalTransactionDto result = rentalTransactionController.applyForProperty(propertyId, inputDto, userId);

        // Assert
        assertEquals(inputDto.getStartDate(), result.getStartDate());
        assertEquals(inputDto.getEndDate(), result.getEndDate());
        verify(rentalTransactionService, times(1)).applyForProperty(propertyId, inputDto, userId);
    }

    @Test
    void viewApplicationStatus_shouldReturnResponseEntityWithListOfRentalTransactionTenantResponseAndStatusOK() {
        // Arrange
        Long userId = 11L;
        List<RentalTransactionTenantResponse> mockApplications = Arrays.asList(
                RentalTransactionTenantResponse.builder().transactionId(101L).status("Pending").build(),
                RentalTransactionTenantResponse.builder().transactionId(102L).status("Approved").build()
        );
        when(rentalTransactionService.viewApplicationStatus(userId)).thenReturn(mockApplications);

        // Act
        ResponseEntity<List<RentalTransactionTenantResponse>> result = rentalTransactionController.viewApplicationStatus(userId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals("Pending", result.getBody().get(0).getStatus());
        assertEquals("Approved", result.getBody().get(1).getStatus());
        verify(rentalTransactionService, times(1)).viewApplicationStatus(userId);
    }

    @Test
    void getAllPropertyById_shouldReturnResponseEntityWithListOfPropertyResponseDtoAndStatusOK() {
        // Arrange
        Long ownerId = 12L;
        List<PropertyResponseDto> mockOwnerProperties = Arrays.asList(
                PropertyResponseDto.builder().propertyId(21L).address(new Address()).build(),
                PropertyResponseDto.builder().propertyId(22L).address(new Address()).build()
        );
        when(propertyService.getAllPropertyById(ownerId)).thenReturn(mockOwnerProperties);

        // Act
        ResponseEntity<List<PropertyResponseDto>> result = rentalTransactionController.getAllPropertyById(ownerId);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(2, result.getBody().size());
        assertEquals(21L, result.getBody().get(0).getPropertyId());
        assertEquals(22L, result.getBody().get(1).getPropertyId());
        verify(propertyService, times(1)).getAllPropertyById(ownerId);
    }
}