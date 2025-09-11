package com.rental.property.controller;

import com.rental.property.dto.OtpRequestDTO;
import com.rental.property.dto.OtpVerifyDTO;
import com.rental.property.dto.PasswordResetDTO;
import com.rental.property.exception.UserNotFoundException;
import com.rental.property.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth/password")
@Slf4j
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    @Operation(summary = "Initiate password reset by sending OTP to email or mobile")
    public ResponseEntity<?> initiatePasswordReset(@Valid @RequestBody OtpRequestDTO otpRequest) {
        try {
            passwordResetService.initiatePasswordReset(otpRequest);
            return ResponseEntity.ok("OTP sent successfully to " + otpRequest.getIdentifier());
        } catch (UserNotFoundException e) {
            log.warn("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error initiating password reset: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send OTP: " + e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP for password reset")
    public ResponseEntity<String> verifyOtp(@Valid @RequestBody OtpVerifyDTO otpVerify) {
        try {
            passwordResetService.verifyOtp(otpVerify);
            return ResponseEntity.ok("OTP verified successfully");
        } catch (Exception e) {
            log.error("Error verifying OTP: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/reset")
    @Operation(summary = "Reset password after OTP verification")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO passwordReset) {
        try {
            passwordResetService.resetPassword(passwordReset);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("Validation errors: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}