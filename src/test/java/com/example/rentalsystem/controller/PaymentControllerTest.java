//package com.example.rentalsystem.controller;
//
//import com.rental.property.controller.PaymentController;
//import com.rental.property.dto.PaymentDTO;
//import com.rental.property.dto.PaymentTenantResponse;
//import com.rental.property.service.PaymentService;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = com.rental.property.RentHubApplication.class)
//public class PaymentControllerTest {
//
//    @InjectMocks
//    private PaymentController paymentController;
//
//    @Mock
//    private PaymentService paymentService;
//
//    @Test
//    void createPayment_shouldReturnOkAndCreatedPaymentDTO() {
//        Long leaseId = 1L;
//        PaymentDTO requestDTO = PaymentDTO.builder()
//                .amount(15000.0)
//                .paymentDate(LocalDate.now())
//                .build();
//        PaymentDTO expectedResponse = PaymentDTO.builder()
//                .paymentId(1L)
//                .amount(15000.0)
//                .paymentDate(LocalDate.now())
//                .status("Pending")
//                .build();
//
//        when(paymentService.createPayment(leaseId, requestDTO)).thenReturn(expectedResponse);
//
//        ResponseEntity<PaymentDTO> response = paymentController.createPayment(leaseId, requestDTO);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//        assertEquals("Pending", requestDTO.getStatus());
//    }
//
//    @Test
//    void getPayment_shouldReturnOkAndPaymentDTO() {
//        Long paymentId = 1L;
//        PaymentDTO expectedResponse = PaymentDTO.builder()
//                .paymentId(paymentId)
//                .amount(15000.0)
//                .paymentDate(LocalDate.now())
//                .status("Completed")
//                .build();
//
//        when(paymentService.getPaymentById(paymentId)).thenReturn(expectedResponse);
//
//        ResponseEntity<PaymentDTO> response = paymentController.getPayment(paymentId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//    }
//
//    @Test
//    void getAllPaymentsByProperty_shouldReturnOkAndListOfPaymentDTOs() {
//        Long propertyId = 10L;
//        List<PaymentDTO> expectedResponse = Arrays.asList(
//                PaymentDTO.builder().paymentId(1L).amount(15000.0).paymentDate(LocalDate.now()).status("Completed").build(),
//                PaymentDTO.builder().paymentId(2L).amount(16000.0).paymentDate(LocalDate.now().plusDays(30)).status("Completed").build()
//        );
//
//        when(paymentService.getAllPaymentsByPropertyId(propertyId)).thenReturn(expectedResponse);
//
//        ResponseEntity<List<PaymentDTO>> response = paymentController.getAllPaymentsByProperty(propertyId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//        assertEquals(2, response.getBody().size());
//    }
//
//    @Test
//    void getAllPaymentsByPropertyAndTransactionId_shouldReturnOkAndListOfPaymentDTOs() {
//        Long propertyId = 10L;
//        Long transactionId = 100L;
//        List<PaymentDTO> expectedResponse = Arrays.asList(
//                PaymentDTO.builder().paymentId(1L).amount(15000.0).paymentDate(LocalDate.now()).status("Completed").build()
//        );
//
//        when(paymentService.getPaymentsByPropertyIdAndTransactionId(propertyId, transactionId)).thenReturn(expectedResponse);
//
//        ResponseEntity<List<PaymentDTO>> response = paymentController.getAllPaymentsByPropertyAndTransactionId(propertyId, transactionId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//        assertEquals(1, response.getBody().size());
//    }
//
//    @Test
//    void getAllPaymentByUserId_shouldReturnOkAndListOfPaymentTenantResponse() {
//        Long userId = 1L;
//        List<PaymentTenantResponse> expectedResponse = Arrays.asList(
//                PaymentTenantResponse.builder().amount(15000.0).paymentDate(LocalDate.now()).status("Completed").build(),
//                PaymentTenantResponse.builder().amount(16000.0).paymentDate(LocalDate.now().plusDays(30)).status("Pending").build()
//        );
//
//        when(paymentService.getAllpaymentsByUserId(userId)).thenReturn(expectedResponse);
//
//        ResponseEntity<List<PaymentTenantResponse>> response = paymentController.getAllPaymentByUserId(userId);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(expectedResponse, response.getBody());
//        assertEquals(2, response.getBody().size());
//    }
//}