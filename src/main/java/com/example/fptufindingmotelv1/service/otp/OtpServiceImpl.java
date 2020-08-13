package com.example.fptufindingmotelv1.service.otp;

import com.example.fptufindingmotelv1.untils.Constant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService{

    private LoadingCache<String, char[]> otpCache;

    @Value("${ffm.sms.url}")
    private String eSmsUrl;

    @Value("${ffm.sms.apiKey}")
    private String apiKey;

    @Value("${ffm.sms.secretKey}")
    private String secretKey;

    @Value("${ffm.sms.smsType}")
    private String smsType;

    @Value("${ffm.sms.brandName}")
    private String brandName;

    public OtpServiceImpl() {
        this.otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(Constant.EXPIRE_MIN, TimeUnit.MINUTES).build(new CacheLoader<String, char[]>() {
            @Override
            public char[] load(String s) throws Exception {
                return new char[0];
            }
        });
    }

    @Override
    public char[] generateOTP(String phoneNumber) {
        try {
            char[] otp = new char[Constant.OTP_LENGTH];
            String numbers = "0123456789";
            Random rand = new Random();
            for (int i = 0; i < Constant.OTP_LENGTH; i++) {
                otp[i] = numbers.charAt(rand.nextInt(numbers.length()));
            }
            this.otpCache.put(phoneNumber, otp);
            return otp;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject sendSms(String phoneNumber, char[] otp) {
        try {
//            String content = "Ma OTP cua ban la: " + String.valueOf(otp);
//            JSONObject request = new JSONObject();
//            request.put("ApiKey", apiKey);
//            request.put("SecretKey", secretKey);
//            request.put("SmsType", smsType);
//            request.put("Brandname", brandName);
//            request.put("Phone", phoneNumber);
//            request.put("Content", content);
//
//            RestTemplate restTemplate = new RestTemplate();
//            String response = restTemplate.postForObject(eSmsUrl, request, String.class);
//            JSONParser parser = new JSONParser();
//            return (JSONObject) parser.parse(response);

            // test send sms success
            JSONObject response = new JSONObject();
            response.put("CodeResult", "100");
            response.put("otp", String.valueOf(otp));
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public char[] getOTP(String phoneNumber) {
        try {
            return otpCache.get(phoneNumber);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean clearOTP(String phoneNumber) {
        try {
            otpCache.invalidate(phoneNumber);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
