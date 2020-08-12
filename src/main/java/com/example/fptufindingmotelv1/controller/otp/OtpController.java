package com.example.fptufindingmotelv1.controller.otp;

import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.service.otp.OtpService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @ResponseBody
    @RequestMapping(value = "/api-send-otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject sendOTP(@RequestParam String phoneNumber) {
        boolean existPhone = userRepository.existsByPhoneNumber(phoneNumber);
        if(existPhone){
            char[] otp = otpService.generateOTP(phoneNumber);
            return otpService.sendSms(phoneNumber, otp);
        }else {
            return responseMsg("001", "Số điện thoại của bạn chưa được đăng ký", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-validate-otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject validateOTP(@RequestParam String phoneNumber, @RequestParam String inputOTP) {
        if (inputOTP == null || inputOTP.isEmpty()){
            return responseMsg("001", "Vui lòng nhập mã xác thực", null);
        }
        char[] otp = otpService.getOTP(phoneNumber);
        if(otp == null || otp.length == 0){
            return responseMsg("002", "Mã xác thực đã hết hạn, vui lòng gửi lại mã", null);
        }else if(otp != null && String.valueOf(otp).equals(inputOTP)){
            otpService.clearOTP(phoneNumber);
            return responseMsg("000", "Success", null);
        }else {
            return responseMsg("001", "Mã xác thực không hợp lệ", null);
        }
    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
