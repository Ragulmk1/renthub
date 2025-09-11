package com.example.rentalsystem.exception;

import com.rental.property.exception.GlobalRestAPIExceptionHandler;
import com.rental.property.exception.PropertyNotFoundException;
import com.rental.property.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.NoSuchElementException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalRestAPIExceptionHandlerTest {

    private GlobalRestAPIExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalRestAPIExceptionHandler();
    }

    @Test
    void testHandlePropertyNotFoundException() {
        PropertyNotFoundException exception = new PropertyNotFoundException("Property not found");
        ResponseEntity<Map<String, String>> response = exceptionHandler.handlePropertyNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Property not found", response.getBody().get("error"));
    }

    @Test
    void testHandleNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("No element found");
        ResponseEntity<?> response = exceptionHandler.noSuchElementExcep(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No element found", response.getBody());
    }

    @Test
    void testHandleUserAlreadyExistsException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User already exists");
        ResponseEntity<String> response = exceptionHandler.handleUserAlreadyExistsException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testHandleAuthorizationDeniedException() {
        AuthorizationDeniedException exception = new AuthorizationDeniedException("Forbidden access");
        ResponseEntity<?> response = exceptionHandler.illegalAccessor(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You are not authorized ", response.getBody());
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid argument", response.getBody());
    }
}
