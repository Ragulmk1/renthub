package com.example.rentalsystem.service;

import com.rental.property.dto.OtpRequestDTO;
import com.rental.property.dto.OtpVerifyDTO;
import com.rental.property.dto.PasswordResetDTO;
import com.rental.property.entity.User;
import com.rental.property.exception.UserNotFoundException;
import com.rental.property.repo.UserRepository;
import com.rental.property.service.PasswordResetServiceImpl;
import com.rental.property.util.EmailUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    private User testUser;
    private static final String EMAIL = "test@example.com";
    private static final String MOBILE = "1234567890";
    private static final String VALID_PASSWORD = "Password@123";

    @BeforeEach
    void setUp() throws Exception {
        testUser = new User();
        testUser.setEmail(EMAIL);
        testUser.setMobileNo(Long.parseLong(MOBILE));
        testUser.setPassword("encodedPassword");
        clearOtpStore();
    }

    private void clearOtpStore() throws Exception {
        Field otpStoreField = PasswordResetServiceImpl.class.getDeclaredField("otpStore");
        otpStoreField.setAccessible(true);
        Map<String, Object> otpStore = (Map<String, Object>) otpStoreField.get(passwordResetService);
        otpStore.clear();
        otpStoreField.setAccessible(false);
    }

    private void storeOtp(String email, String otp, LocalDateTime expirationTime) throws Exception {
        Class<?> otpDataClass = Class.forName("com.rental.property.service.PasswordResetServiceImpl$OtpData");

        Constructor<?> constructor = otpDataClass.getDeclaredConstructor(String.class, LocalDateTime.class);
        constructor.setAccessible(true);

        Object otpData = constructor.newInstance(otp, expirationTime);
        constructor.setAccessible(false);

        Field otpStoreField = PasswordResetServiceImpl.class.getDeclaredField("otpStore");
        otpStoreField.setAccessible(true);
        Map<String, Object> otpStore = (Map<String, Object>) otpStoreField.get(passwordResetService);
        otpStore.put(email, otpData);
        otpStoreField.setAccessible(false);
    }

    @Test
    void initiatePasswordReset_WithEmail_Success() {
        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIdentifier(EMAIL);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));

        passwordResetService.initiatePasswordReset(otpRequest);

        verify(userRepository).findByEmail(EMAIL);
        verify(emailUtil).sendOtpEmail(eq(EMAIL), any(String.class));
    }

    @Test
    void initiatePasswordReset_WithMobile_Success() {
        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIdentifier(MOBILE);
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        passwordResetService.initiatePasswordReset(otpRequest);

        verify(userRepository).findAll();
        verify(emailUtil).sendOtpEmail(eq(EMAIL), any(String.class));
    }

    @Test
    void initiatePasswordReset_WithInvalidEmail_ThrowsUserNotFoundException() {
        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIdentifier("nonexistent@example.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                passwordResetService.initiatePasswordReset(otpRequest));
        verify(emailUtil, never()).sendOtpEmail(any(), any());
    }

    @Test
    void initiatePasswordReset_WithInvalidMobile_ThrowsIllegalArgumentException() {
        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIdentifier("invalid");

        assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.initiatePasswordReset(otpRequest));
        verify(emailUtil, never()).sendOtpEmail(any(), any());
    }

    @Test
    void verifyOtp_ValidOtp_Success() throws Exception {

        String[] capturedOtp = new String[1];
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        doAnswer(invocation -> {
            String email = invocation.getArgument(0);
            String otp = invocation.getArgument(1);
            capturedOtp[0] = otp;
            storeOtp(email, otp, LocalDateTime.now().plusMinutes(5));
            return null;
        }).when(emailUtil).sendOtpEmail(anyString(), anyString());

        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIdentifier(EMAIL);
        passwordResetService.initiatePasswordReset(otpRequest);

        OtpVerifyDTO otpVerify = new OtpVerifyDTO(EMAIL, capturedOtp[0]);
        passwordResetService.verifyOtp(otpVerify);

    }

    @Test
    void verifyOtp_InvalidOtp_ThrowsIllegalArgumentException() throws Exception {

        String validOtp = "123456";
        storeOtp(EMAIL, validOtp, LocalDateTime.now().plusMinutes(5));
        OtpVerifyDTO otpVerify = new OtpVerifyDTO(EMAIL, "999999");

        assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.verifyOtp(otpVerify));
    }

    @Test
    void verifyOtp_ExpiredOtp_ThrowsIllegalArgumentException() throws Exception {

        String validOtp = "123456";
        storeOtp(EMAIL, validOtp, LocalDateTime.now().minusMinutes(5));
        OtpVerifyDTO otpVerify = new OtpVerifyDTO(EMAIL, validOtp);


        assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.verifyOtp(otpVerify));
    }

    @Test
    void resetPassword_ValidInput_Success() throws Exception {
        String validOtp = "123456";
        storeOtp(EMAIL, validOtp, LocalDateTime.now().plusMinutes(5));
        PasswordResetDTO passwordReset = new PasswordResetDTO();
        passwordReset.setEmail(EMAIL);
        passwordReset.setOtp(validOtp);
        passwordReset.setNewPassword(VALID_PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn("encodedNewPassword");


        passwordResetService.resetPassword(passwordReset);

        verify(userRepository).save(testUser);
        verify(passwordEncoder).encode(VALID_PASSWORD);
        assertEquals("encodedNewPassword", testUser.getPassword());
    }

    @Test
    void resetPassword_InvalidPassword_ThrowsIllegalArgumentException() throws Exception {
        String validOtp = "123456";
        storeOtp(EMAIL, validOtp, LocalDateTime.now().plusMinutes(5));
        PasswordResetDTO passwordReset = new PasswordResetDTO();
        passwordReset.setEmail(EMAIL);
        passwordReset.setOtp(validOtp);
        passwordReset.setNewPassword("weak");
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () ->
                passwordResetService.resetPassword(passwordReset));
        verify(userRepository, never()).save(any());
    }

    @Test
    void resetPassword_UserNotFound_ThrowsUserNotFoundException() throws Exception {

        String validOtp = "123456";
        storeOtp(EMAIL, validOtp, LocalDateTime.now().plusMinutes(5));
        PasswordResetDTO passwordReset = new PasswordResetDTO();
        passwordReset.setEmail(EMAIL);
        passwordReset.setOtp(validOtp);
        passwordReset.setNewPassword(VALID_PASSWORD);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                passwordResetService.resetPassword(passwordReset));
        verify(userRepository, never()).save(any());
    }
}