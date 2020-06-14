package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.repository.UserModelRepository;
import com.example.fptufindingmotelv1.service.register.RegisterService;
import com.restfb.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserModelRepository userModelRepository;

    @RequestMapping(value = "/dang-ki", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    @RequestMapping(value = "/validRegister", method = RequestMethod.POST)
    public String validateRegister(Model model, @RequestParam(value = "registerModel") String registerModel) {
        System.out.println(Json.parse("registerModel"));

        return "error";
    }

    public Boolean validateUsername(String userName) {
        return false;
    }

    @ResponseBody
    @PostMapping("/api/get-otp")
    public char[] getOTP(@RequestParam int otpLength) {
        return registerService.generateOTP(6);
    }
}



