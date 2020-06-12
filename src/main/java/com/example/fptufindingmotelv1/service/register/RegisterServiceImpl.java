package com.example.fptufindingmotelv1.service.register;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService{
    @Override
    public char[] generateOTP(int length) {
        char[] otp = new char[length];
        String numbers = "0123456789";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(rand.nextInt(numbers.length()));
        }
        return otp;
    }
}
