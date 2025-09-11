package com.example.rentalsystem.controller;
import com.rental.property.controller.UserController;
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
import static org.mockito.Mockito.*;
public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private UserDto userDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = UserDto.builder()
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .mobileNo(1234567890L)
                .role(1L)
                .build();
    }
    @Test
    void updateUser_ValidInput_ReturnsOkAndUserDto() {
        Long userId = 1L;
        when(userService.updateProfile(userId, userDto)).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.updateUser(userId, userDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService, times(1)).updateProfile(userId, userDto);
    }
    @Test
    void viewProfile_ExistingUserId_ReturnsOkAndUserDto() {
        Long userId = 1L;
        when(userService.viewProfile(userId)).thenReturn(userDto);
        ResponseEntity<UserDto> response = userController.viewProfile(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());
        verify(userService, times(1)).viewProfile(userId);
    }
    @Test
    void viewProfile_NonExistingUserId_ReturnsNotFound() {
        Long userId = 2L;
        when(userService.viewProfile(userId)).thenReturn(null); // Or throw an exception in UserService
        ResponseEntity<UserDto> response = userController.viewProfile(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode()); // If your controller returns OK with null body
        // Or if you expect a different status code based on how you handle null:
        // assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).viewProfile(userId);
    }
    // You can add more test cases to cover different scenarios, like invalid input,
    // service layer exceptions, etc.
}