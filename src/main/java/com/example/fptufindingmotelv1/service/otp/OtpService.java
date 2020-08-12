package com.example.fptufindingmotelv1.service.otp;

import net.minidev.json.JSONObject;

public interface OtpService {
    char[] generateOTP(String phoneNumber);
    JSONObject sendSms(String phoneNumber, char[] otp);
    char[] getOTP(String phoneNumber);
    boolean clearOTP(String phoneNumber);
}
