package com.rental.property.service;

import com.rental.property.dto.OtpRequestDTO;
import com.rental.property.dto.OtpVerifyDTO;
import com.rental.property.dto.PasswordResetDTO;
import com.rental.property.entity.User;
import com.rental.property.exception.UserNotFoundException;
import com.rental.property.repo.UserRepository;
import com.rental.property.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final EmailUtil emailUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${otp.expiration.minutes:10}")
    private int otpExpirationMinutes;

    private final Map<String, OtpData> otpStore = new HashMap<>();

    @Override
    public void initiatePasswordReset(OtpRequestDTO otpRequest) {
        User user = findUserByIdentifier(otpRequest.getIdentifier());
        String otp = generateOtp();
        storeOtp(user.getEmail(), otp);
        emailUtil.sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    public void verifyOtp(OtpVerifyDTO otpVerify) {
        OtpData otpData = otpStore.get(otpVerify.getEmail());
        if (otpData == null || !otpData.getOtp().equals(otpVerify.getOtp())) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        if (otpData.getExpirationTime().isBefore(LocalDateTime.now())) {
            otpStore.remove(otpVerify.getEmail());
            throw new IllegalArgumentException("OTP has expired");
        }
    }

    @Override
    public void resetPassword(PasswordResetDTO passwordReset) {
        verifyOtp(new OtpVerifyDTO(passwordReset.getEmail(), passwordReset.getOtp()));
        User user = userRepository.findByEmail(passwordReset.getEmail())
                .map(u -> (User) u)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!isValidPassword(passwordReset.getNewPassword())) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.");
        }

        user.setPassword(passwordEncoder.encode(passwordReset.getNewPassword()));
        userRepository.save(user);
        otpStore.remove(passwordReset.getEmail());
    }

    private User findUserByIdentifier(String identifier) {
        if (identifier.contains("@")) {
            return userRepository.findByEmail(identifier)
                    .map(u -> (User) u)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + identifier));
        } else {
            try {
                Long mobileNo = Long.parseLong(identifier);
                return userRepository.findAll().stream()
                        .filter(u -> u.getMobileNo().equals(mobileNo))
                        .findFirst()
                        .orElseThrow(() -> new UserNotFoundException("User not found with mobile number: " + identifier));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid mobile number format");
            }
        }
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void storeOtp(String email, String otp) {
        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(otpExpirationMinutes)));
    }

    private boolean isValidPassword(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password != null && Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();
    }

    private static class OtpData {
        private final String otp;
        private final LocalDateTime expirationTime;

        public OtpData(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}