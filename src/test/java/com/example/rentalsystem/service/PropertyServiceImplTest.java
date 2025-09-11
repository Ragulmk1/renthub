package com.example.rentalsystem.service;

import com.rental.property.dto.PropertyRequestDto;
import com.rental.property.dto.PropertyResponseDto;
import com.rental.property.entity.Property;
import com.rental.property.entity.User;
import com.rental.property.exception.PropertyNotFoundException;
import com.rental.property.repo.PropertyRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.PropertyServiceImpl;
import com.rental.property.util.EntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyServiceImplTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private PropertyServiceImpl propertyService;

    private Property property;
    private User landlord;
    private PropertyRequestDto propertyRequestDto;

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test_landlord");

        SecurityContextHolder.setContext(securityContext);

        landlord = new User();
        landlord.setId(1L);
        landlord.setUsername("test_landlord");

        lenient().when(userRepository.findByUsername("test_landlord")).thenReturn(Optional.of(landlord));
    }

    @BeforeEach
    void setUp() {
        property = new Property();
        property.setPropertyId(1L);
        property.setUser(landlord);

        propertyRequestDto = new PropertyRequestDto();
    }



    @Test
    void testUpdateProperty_NotOwnedByCurrentUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        property.setUser(otherUser);
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        assertThrows(RuntimeException.class, () -> propertyService.updateProperty(1L, propertyRequestDto));
    }

    @Test
    void testDeleteProperty_NotOwnedByCurrentUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        property.setUser(otherUser);
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

        assertThrows(RuntimeException.class, () -> propertyService.deleteProperty(1L));
    }
}
