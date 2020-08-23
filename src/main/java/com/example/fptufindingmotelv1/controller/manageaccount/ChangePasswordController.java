package com.example.fptufindingmotelv1.controller.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.service.manageaccount.ChangePasswordService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangePasswordController {

    @Autowired
    ChangePasswordService changePasswordService;

    @ResponseBody
    @PostMapping("/api-change-password")
    public JSONObject savePassword(@RequestBody UserDTO request){
        return changePasswordService.savePassword(request);
    }
}
