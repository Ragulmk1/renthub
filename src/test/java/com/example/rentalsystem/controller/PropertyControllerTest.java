package com.example.rentalsystem.controller;

import com.rental.property.controller.PropertyController;
import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.dto.RentalTransactionResponseDto;
import com.rental.property.exception.PropertyNotFoundException;
import com.rental.property.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyControllerTest {

    @InjectMocks
    private PropertyController propertyController;

    @Mock
    private PropertyService propertyService;

    @Test
    void addNewProperty_ValidInput_ReturnsOk() throws IOException {
        PropertyRequestDto requestDto = new PropertyRequestDto();
        MultipartFile image = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());
        PropertyResponseDto responseDto = new PropertyResponseDto();
        when(propertyService.addNewProperty(requestDto, image)).thenReturn(responseDto);

        ResponseEntity<PropertyResponseDto> response = propertyController.addNewProperty(requestDto, image);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto, response.getBody());
        verify(propertyService, times(1)).addNewProperty(requestDto, image);
    }

    @Test
    void getAllPropertyByLandLordId_ExistingId_ReturnsOkWithPropertyList() {
        Long landlordId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<PropertyResponseDto> propertyList = Arrays.asList(new PropertyResponseDto(), new PropertyResponseDto());
        when(propertyService.getAllPropertyByLandLordId(landlordId, pageable)).thenReturn(propertyList);

        ResponseEntity<List<PropertyResponseDto>> response = propertyController.getAllPropertyByLandLordId(landlordId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(propertyList.size(), response.getBody().size());
        assertEquals(propertyList, response.getBody());
        verify(propertyService, times(1)).getAllPropertyByLandLordId(landlordId, pageable);
    }

    @Test
    void getPropertyById_ExistingId_ReturnsOkWithProperty() {
        Long propertyId = 1L;
        PropertyResponseDto responseDto = new PropertyResponseDto();
        when(propertyService.getPropertyById(propertyId)).thenReturn(responseDto);

        ResponseEntity<PropertyResponseDto> response = propertyController.getPropertyById(propertyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDto, response.getBody());
        verify(propertyService, times(1)).getPropertyById(propertyId);
    }

    @Test
    void updateProperty_ExistingId_ReturnsOkWithUpdatedProperty() {
        Long propertyId = 1L;
        PropertyRequestDto requestDto = new PropertyRequestDto();
        PropertyResponseDto updatedDto = new PropertyResponseDto();
        when(propertyService.updateProperty(propertyId, requestDto)).thenReturn(updatedDto);

        ResponseEntity<PropertyResponseDto> response = propertyController.updateProperty(propertyId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedDto, response.getBody());
        verify(propertyService, times(1)).updateProperty(propertyId, requestDto);
    }

    @Test
    void deleteProperty_ExistingId_ReturnsNotFoundAfterDeletion() {
        Long propertyId = 1L;
        doNothing().when(propertyService).deleteProperty(propertyId);

        ResponseEntity<?> response = propertyController.deleteProperty(propertyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(propertyService, times(1)).deleteProperty(propertyId);
    }

    @Test
    void deleteProperty_NonExistingId_ReturnsNotFound() {
        Long propertyId = 1L;
        doThrow(new PropertyNotFoundException("Property not found")).when(propertyService).deleteProperty(propertyId);

        ResponseEntity<?> response = propertyController.deleteProperty(propertyId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(propertyService, times(1)).deleteProperty(propertyId);
    }

    @Test
    void acceptApplication_ValidInput_ReturnsOk() {
        Long tenantId = 1L;
        String status = "ACCEPTED";
        doNothing().when(propertyService).updateApplicationStatus(tenantId, status);

        ResponseEntity<?> response = propertyController.acceptApplication(tenantId, status);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(propertyService, times(1)).updateApplicationStatus(tenantId, status);
    }

    @Test
    void getAllTenantTransactions_ExistingPropertyId_ReturnsOkWithTransactionList() {
        Long propertyId = 1L;
        List<RentalTransactionResponseDto> transactionList = Arrays.asList(new RentalTransactionResponseDto(), new RentalTransactionResponseDto());
        when(propertyService.getAllTenantTransactions(propertyId)).thenReturn(transactionList);

        ResponseEntity<List<RentalTransactionResponseDto>> response = propertyController.getAllTenantTransactions(propertyId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionList.size(), response.getBody().size());
        assertEquals(transactionList, response.getBody());
        verify(propertyService, times(1)).getAllTenantTransactions(propertyId);
    }

    @Test
    void getAllTenantTransactionsByLandLordId_ExistingLandlordId_ReturnsOkWithTransactionList() {
        Long landlordId = 1L;
        List<RentalTransactionResponseDto> transactionList = Arrays.asList(new RentalTransactionResponseDto(), new RentalTransactionResponseDto());
        when(propertyService.getAllTenantTransactionsByLandLordId(landlordId)).thenReturn(transactionList);

        ResponseEntity<List<RentalTransactionResponseDto>> response = propertyController.getAllTenantTransactionsByLandLordId(landlordId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(transactionList.size(), response.getBody().size());
        assertEquals(transactionList, response.getBody());
        verify(propertyService, times(1)).getAllTenantTransactionsByLandLordId(landlordId);
    }
}