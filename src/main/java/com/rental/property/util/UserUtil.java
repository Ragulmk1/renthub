package com.rental.property.util;
import com.rental.property.dto.UserDto;
import com.rental.property.entity.User;
import org.springframework.stereotype.Component;
@Component
public class UserUtil {
    public User convertUserDtoToUser(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .mobileNo(userDto.getMobileNo())
                .role(userDto.getRole()).build();
    }
    public UserDto convertUserToUserDto(User user){
        return UserDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobileNo(user.getMobileNo())
                .role(user.getRole()).build();
    }
}
