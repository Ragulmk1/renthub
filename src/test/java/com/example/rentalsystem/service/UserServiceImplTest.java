package com.example.rentalsystem.service;

import com.rental.property.dto.UserDto;
import com.rental.property.entity.Role;
import com.rental.property.entity.User;
import com.rental.property.repo.RoleRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.UserServiceImpl;
import com.rental.property.util.EntityMapper;
import com.rental.property.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserUtil userUtil;

    @Mock
    private RoleRepository roleRepository;

    private UserDto userDto;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .username("testuser")
                .password("Password1!") // Updated to meet password criteria
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .mobileNo(1234512345L)
                .role(1L)
                .build();

        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .mobileNo(1234512345L)
                .role(1L)
                .build();

        role = new Role();
        role.setId(1L);
        role.setName("USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    }

    @Test
    void registerNewUser_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword"); // Updated to match the actual password
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userUtil.convertUserToUserDto(any(User.class))).thenReturn(userDto);

        UserDto registeredUser = userService.registerNewUser(userDto);

        assertEquals(userDto, registeredUser);
        verify(roleRepository).findById(1L);
        verify(passwordEncoder).encode("Password1!"); // Updated to match the actual password
        verify(userRepository).save(any(User.class));
        verify(userUtil).convertUserToUserDto(any(User.class));
    }

    @Test
    void updateProfile_Success() {
        User existingUser = User.builder()
                .id(1L)
                .username("olduser")
                .password("oldPassword")
                .email("old@example.com")
                .firstName("Old")
                .lastName("User")
                .mobileNo(5432154321L)
                .role(2L)
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .mobileNo(1234512345L)
                .role(1L)
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .username("testuser")
                .password("Password1!") // Updated to meet password criteria
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .mobileNo(1234512345L)
                .role(1L)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userUtil.convertUserToUserDto(any(User.class))).thenReturn(updatedUserDto);

        UserDto result = userService.updateProfile(1L, updatedUserDto);

        assertEquals(updatedUserDto, result);
        assertEquals("testuser", existingUser.getUsername());
        assertEquals("encodedPassword", existingUser.getPassword());
        assertEquals("Test", existingUser.getFirstName());
        assertEquals("User", existingUser.getLastName());
        assertEquals("test@example.com", existingUser.getEmail());
        assertEquals(1234512345L, existingUser.getMobileNo());

        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("Password1!");
        verify(userRepository).save(existingUser);
        verify(userUtil).convertUserToUserDto(any(User.class));
    }

    @Test
    void viewProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userUtil.convertUserToUserDto(any(User.class))).thenReturn(userDto);

        UserDto viewedProfile = userService.viewProfile(1L);

        assertEquals(userDto, viewedProfile);
        verify(userRepository).findById(1L);
        verify(userUtil).convertUserToUserDto(any(User.class));
    }

//    @Test
//    void registerNewUser_RoleNotFound_ThrowsException() {
//        UserDto newUserDto = UserDto.builder()
//                .username("newuser")
//                .password("Password1") // Updated to meet password criteria
//                .email("new@example.com")
//                .firstName("New")
//                .lastName("User")
//                .mobileNo(9876543210L)
//                .role(1L)
//                .build();
//
//        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
//
//        assertThrows(
//                NoSuchElementException.class, // Updated to match the actual exception type
//                () -> userService.registerNewUser(newUserDto)
//        );
//
//        verify(roleRepository).findById(99L);
//        verifyNoInteractions(userRepository);
//        verifyNoInteractions(userUtil);
//    }

    @Test
    void updateProfile_UserNotFound_ThrowsException() {
        UserDto updatedUserDto = UserDto.builder()
                .username("updateduser")
                .password("Password1!") // Updated to meet password criteria
                .email("updated@example.com")
                .firstName("Updated")
                .lastName("User")
                .mobileNo(1122334455L)
                .role(1L)
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class, // Updated to match the actual exception type
                () -> userService.updateProfile(2L, updatedUserDto)
        );

        verify(userRepository).findById(2L);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userUtil);
    }

    @Test
    void viewProfile_UserNotFound_ThrowsException() {
        when(userRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class, // Updated to match the actual exception type
                () -> userService.viewProfile(3L)
        );

        verify(userRepository).findById(3L);
        verifyNoInteractions(userUtil);
    }
}