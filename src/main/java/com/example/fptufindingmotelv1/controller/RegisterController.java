package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.repository.UserModelRepository;
import com.example.fptufindingmotelv1.service.register.RegisterService;
import com.restfb.json.Json;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserModelRepository userModelRepository;

    @GetMapping("/dang-ki")
    public String getRegister() {
        return "register";
    }

    @ResponseBody
    @PostMapping("/validRegister")
    public String validateRegister(Model model, @RequestBody UserDTO userDTO) {
        JSONObject registerMsg = new JSONObject();
        if (userModelRepository.existsByUsername(userDTO.getUsername())) {
            registerMsg.put("code", "1");
            registerMsg.put("message", "Username existed!");
        } else if (userModelRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
            registerMsg.put("code", "2");
            registerMsg.put("message", "Phone existed!");
        } else if (registerService.save(userDTO).getUsername() != null) {
            registerMsg.put("code", "0");
            registerMsg.put("message", "Register success!");
        }

        return registerMsg.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/api/get-otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public char[] getOTP(@RequestParam int otpLength) {
        return registerService.generateOTP(otpLength);
    }
}



