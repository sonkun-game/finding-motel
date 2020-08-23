package com.example.fptufindingmotelv1.controller.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.service.manageaccount.ChangeUserInformationService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangeUserInformationController {

    @Autowired
    private ChangeUserInformationService changeUserInformationService;

    @ResponseBody
    @PostMapping("/api-save-user-info")
    public JSONObject saveUserInfo(@RequestBody UserDTO request){
        JSONObject response = new JSONObject();
        if(changeUserInformationService.saveUserInfo(request)){
            response.put("msgCode", "user000");
            response.put("message", "Update user information successfully");
        }else {
            response.put("msgCode", "sys999");
            response.put("message", "System Error");
        }
        return response;
    }

}
