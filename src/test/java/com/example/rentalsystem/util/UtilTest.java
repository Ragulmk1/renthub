package com.example.rentalsystem.util;

import com.rental.property.dto.UserDto;
import com.rental.property.entity.User;
import com.rental.property.util.UserUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UtilTest {

    private final UserUtil userUtil = new UserUtil();

    @Test
    void convertUserDtoToUser_shouldMapAllFieldsCorrectly() {
        UserDto userDto = UserDto.builder()
                .username("testuser")
                .password("testpass")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .mobileNo(1234567890L)
                .role(1L)
                .build();

        User user = userUtil.convertUserDtoToUser(userDto);

        assertEquals(userDto.getUsername(), user.getUsername());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getMobileNo(), user.getMobileNo());
        assertEquals(userDto.getRole(), user.getRole());
    }

    @Test
    void convertUserDtoToUser_shouldHandleNullFieldsGracefully() {
        UserDto userDto = UserDto.builder().build();
        User user = userUtil.convertUserDtoToUser(userDto);
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getMobileNo());
        assertNull(user.getRole());
    }

    @Test
    void convertUserToUserDto_shouldMapAllFieldsCorrectly() {
        User user = User.builder()
                .username("testuser")
                .password("testpass")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .mobileNo(1234567890L)
                .role(1L)
                .build();

        UserDto userDto = userUtil.convertUserToUserDto(user);

        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.getMobileNo(), userDto.getMobileNo());
        assertEquals(user.getRole(), userDto.getRole());
    }

    @Test
    void convertUserToUserDto_shouldHandleNullFieldsGracefully() {
        User user = User.builder().build();
        UserDto userDto = userUtil.convertUserToUserDto(user);
        assertNull(userDto.getUsername());
        assertNull(userDto.getPassword());
        assertNull(userDto.getEmail());
        assertNull(userDto.getFirstName());
        assertNull(userDto.getLastName());
        assertNull(userDto.getMobileNo());
        assertNull(userDto.getRole());
    }
}