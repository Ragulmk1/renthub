package com.rental.property.service;

import com.rental.property.dto.UserDto;
import com.rental.property.entity.Role;
import com.rental.property.entity.User;
import com.rental.property.exception.UserAlreadyExistsException;
import com.rental.property.repo.RoleRepository;
import com.rental.property.repo.UserRepository;
import com.rental.property.util.EntityMapper;
import com.rental.property.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private RoleRepository roleRepository;


    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public UserDto registerNewUser(UserDto userDto) {
        if (!isValidPassword(userDto.getPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username '" + userDto.getUsername() + "' already exists.");
        }

        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email '" + userDto.getEmail() + "' is already registered.");
        }
        if(userRepository.findByMobileNo(userDto.getMobileNo()).isPresent()){
            throw new UserAlreadyExistsException("Mobile No '" +userDto.getMobileNo() + "' is already registered try another valid Mobile Number");
        }
        Set<Role> roleSet=new HashSet<>();

        Role role=roleRepository.findById(userDto.getRole()).get();
        roleSet.add(role);
        User user=User.builder().username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .mobileNo(userDto.getMobileNo())
                .role(userDto.getRole())
                .roles(roleSet).build();
        userRepository.save(user);
        return userUtil.convertUserToUserDto(user);
    }

    @Override
    public UserDto updateProfile(Long id, UserDto userDto) {
        User user =userRepository.findById(id).get();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setMobileNo(userDto.getMobileNo());
        userRepository.save(user);
        return userUtil.convertUserToUserDto(user);
    }

    @Override
    public UserDto viewProfile(Long id) {
        User user=userRepository.findById(id).get();
        return userUtil.convertUserToUserDto(user);
    }
    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


}
