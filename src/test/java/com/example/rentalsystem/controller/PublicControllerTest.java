package com.example.rentalsystem.controller;
import com.rental.property.controller.PublicController;
import com.rental.property.dto.UserDto;
import com.rental.property.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
public class PublicControllerTest {
    @InjectMocks //This annotation tells Mockito to inject the mocks into this class
    private PublicController publicController;
    @Mock //This annotation tells Mockito to create a mock instance of UserService
    private UserService userService;
    private UserDto userDto;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); //This line is crucial for initializing the mocks
        userDto = UserDto.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .firstName("Saveetha")
                .lastName("Sankar")
                .mobileNo(9876543211L)
                .role(1L)
                .build();
    }
    @Test
    public void testRegisterUser_Success() {
        when(userService.registerNewUser(any(UserDto.class))).thenReturn(userDto);
        ResponseEntity<?> responseEntity = publicController.registerNewUser(userDto);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode()); // Changed to HttpStatus.OK
    }
}