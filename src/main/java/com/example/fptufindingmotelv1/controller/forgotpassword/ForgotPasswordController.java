package com.example.fptufindingmotelv1.controller.forgotpassword;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.service.forgotpassword.ForgotPasswordService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ForgotPasswordController {

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot")
    public String getForgot(){
        return "forgot";
    }

    @ResponseBody
    @PostMapping(value = "/api-save-new-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject saveNewPassword(@RequestBody UserDTO userDTO){
        JSONObject response = new JSONObject();
        if(forgotPasswordService.saveNewPassword(userDTO)){
            response.put("msgCode", "forgot000");
            response.put("message", "Save password successfully");
        }else{
            response.put("msgCode", "sys999");
            response.put("message", "System Error");
        }
        return response;
    }
}
