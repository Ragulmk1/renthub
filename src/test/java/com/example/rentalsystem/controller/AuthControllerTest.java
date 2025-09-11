package com.example.rentalsystem.controller;
import com.rental.property.controller.AuthController;
import com.rental.property.dto.AuthRequestDTO;
import com.rental.property.dto.AuthResponseDTO;
import com.rental.property.dto.CustomUserDetails;
import com.rental.property.entity.Role;
import com.rental.property.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthController authController;
    @Test
    void testLoginSuccessful() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");
        com.rental.property.entity.User user = createUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mockedJwtToken");
        ResponseEntity<?> response = authController.login(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        AuthResponseDTO authResponse = (AuthResponseDTO) response.getBody();
        assertEquals("mockedJwtToken", authResponse.getJwt());
        assertEquals("test@example.com", authResponse.getEmail());
        assertEquals("Test", authResponse.getFirstName());
        assertEquals("User", authResponse.getLastName());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("testuser");
        verify(jwtUtil, times(1)).generateToken(userDetails);
    }
    @Test
    void testLoginInvalidCredentials() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("wronguser");
        authRequest.setPassword("wrongpassword");
        doThrow(new BadCredentialsException("Invalid username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        ResponseEntity<?> response = authController.login(authRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid username or password", response.getBody());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(jwtUtil, times(0)).generateToken(any());
    }
    @Test
    void testLoginAuthenticationError() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");
        doThrow(new RuntimeException("Authentication failed"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        ResponseEntity<?> response = authController.login(authRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Authentication error: Authentication failed", response.getBody());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(0)).loadUserByUsername(anyString());
        verify(jwtUtil, times(0)).generateToken(any());
    }
    private com.rental.property.entity.User createUser() {
        com.rental.property.entity.User user = new com.rental.property.entity.User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        Set<Role> roles = new HashSet<>();
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        roles.add(userRole);
        user.setRoles(roles);
        return user;
    }
}