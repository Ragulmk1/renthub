package com.example.rentalsystem.service;
import com.rental.property.dto.CustomUserDetails;
import com.rental.property.entity.Role;
import com.rental.property.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
public class CustomUserDetailsTest {
    private CustomUserDetails customUserDetails;
    private User testUser;
    @BeforeEach
    void setUp() {
        // Arrange - Creating a mock user with roles
        Role role = new Role();
        role.setName("ROLE_USER");
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("testpassword");
        testUser.setEmail("testuser@example.com");
        testUser.setRoles(Collections.singleton(role));
        customUserDetails = new CustomUserDetails(testUser);
    }
    @Test
    void shouldReturnCorrectUsername() {
        assertEquals("testuser", customUserDetails.getUsername());
    }
    @Test
    void shouldReturnCorrectPassword() {
        assertEquals("testpassword", customUserDetails.getPassword());
    }
    @Test
    void shouldReturnCorrectEmail() {
        assertEquals("testuser@example.com", customUserDetails.getEmail());
    }
    @Test
    void shouldReturnCorrectAuthorities() {
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) customUserDetails.getAuthorities();
        assertEquals(1, authorities.size()); // Ensure one role exists
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))); // Fix the assertion
    }
    @Test
    void shouldReturnTrueForAccountNonExpired() {
        assertTrue(customUserDetails.isAccountNonExpired());
    }
    @Test
    void shouldReturnTrueForAccountNonLocked() {
        assertTrue(customUserDetails.isAccountNonLocked());
    }
    @Test
    void shouldReturnTrueForCredentialsNonExpired() {
        assertTrue(customUserDetails.isCredentialsNonExpired());
    }
    @Test
    void shouldReturnTrueForEnabled() {
        assertTrue(customUserDetails.isEnabled());
    }
}