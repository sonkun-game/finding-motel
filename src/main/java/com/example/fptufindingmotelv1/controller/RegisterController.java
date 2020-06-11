package com.example.fptufindingmotelv1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {
    @RequestMapping(value = "/dang-ki", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = "/valRegister", method = RequestMethod.POST)
    public String validateRegister(Model model, @RequestParam(value = "userName") String userName
            ,@RequestParam(value = "displayName") String displayName
            ,@RequestParam(value = "phone") String phone
            ,@RequestParam(value = "otpCode") String otpCode
            ,@RequestParam(value = "password") String password
            ,@RequestParam(value = "confirmPassword") String confirmPassword
            ,@RequestParam(value = "role") String role) {
        //To do
        Boolean checkPwd = password.equals(confirmPassword);
        if (checkPwd) {

        }
        return "login";
    }

    public Boolean getAnUser(String userName) {
        return false;
    }
}



