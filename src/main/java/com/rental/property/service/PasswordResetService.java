package com.rental.property.service;

import com.rental.property.dto.OtpRequestDTO;
import com.rental.property.dto.OtpVerifyDTO;
import com.rental.property.dto.PasswordResetDTO;

public interface PasswordResetService {
    void initiatePasswordReset(OtpRequestDTO otpRequest);
    void verifyOtp(OtpVerifyDTO otpVerify);
    void resetPassword(PasswordResetDTO passwordReset);
}