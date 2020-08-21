package com.example.fptufindingmotelv1.controller.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.service.manageaccount.ChangePhoneNumberService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangePhoneNumberController {

    @Autowired
    ChangePhoneNumberService changePhoneNumberService;

    @ResponseBody
    @PostMapping("/api-change-phone-number")
    public JSONObject savePhoneNumber(@RequestBody UserDTO request){
        JSONObject response = new JSONObject();
        if(changePhoneNumberService.savePhone(request)){
            response.put("msgCode", "user000");
            response.put("message", "Change phone successfully");
        }else {
            response.put("msgCode", "sys999");
            response.put("message", "Lỗi hệ thống!");
        }
        return response;
    }
}
