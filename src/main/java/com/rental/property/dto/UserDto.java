package com.rental.property.dto;
import com.rental.property.entity.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;
    @NotNull(message = "Mobile number cannot be null")
    @Digits(integer = 10, fraction = 0, message = "Mobile number should be a valid 10-digit number")
    private Long mobileNo;
    @NotNull(message = "Role cannot be null")
    private Long role;

}
