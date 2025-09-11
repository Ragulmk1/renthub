package com.example.rentalsystem.service;
import com.rental.property.dto.CustomUserDetails;
import com.rental.property.entity.User;
import com.rental.property.entity.Role;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }
    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        Role role = new Role();
        role.setName("ROLE_LANDLORD");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // Act
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        java.util.Collection<? extends SimpleGrantedAuthority> authoritiesCollection = (java.util.Collection<? extends SimpleGrantedAuthority>) userDetails.getAuthorities();
        assertEquals(1, authoritiesCollection.size());
        assertTrue(authoritiesCollection.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_LANDLORD")));
        verify(userRepository, times(1)).findByUsername(username);
    }
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
        verify(userRepository, times(1)).findByUsername(username);
    }
}