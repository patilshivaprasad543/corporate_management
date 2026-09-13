package com.corporate.travel.provider;

public interface OtpProvider {
    void sendOtp(String email, String otp, String purpose);
}
