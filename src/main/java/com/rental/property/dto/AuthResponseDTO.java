package com.rental.property.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class  AuthResponseDTO {
    private Long userId;
    private String jwt;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
