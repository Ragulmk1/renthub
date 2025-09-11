package com.example.rentalsystem.util;

import com.rental.property.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "mysecretkey123456789012345678901234567890";
    private final Long expiration = 1000L;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setSecret(secret);
        jwtUtil.setExpiration(expiration);

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        Date expirationDate = jwtUtil.extractExpiration(token);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        jwtUtil.setExpiration(1L);
        String token = jwtUtil.generateToken(userDetails);
        Thread.sleep(10);
        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testIsTokenExpired() {
        String token = jwtUtil.generateToken(userDetails);
        Date expirationDate = jwtUtil.extractExpiration(token);
        assertNotNull(expirationDate);
        assertTrue(jwtUtil.validateToken(token, userDetails));

        jwtUtil.setExpiration(1L);
        token = jwtUtil.generateToken(userDetails);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertFalse(jwtUtil.validateToken(token, userDetails));
    }
}
