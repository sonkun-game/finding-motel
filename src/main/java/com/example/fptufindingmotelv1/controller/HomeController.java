package com.example.fptufindingmotelv1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomepage(Model model){
        return "index";
    }
    @GetMapping("/dang-nhap")
    public String getLogin(Model model){
        return "login";
    }
    @GetMapping("/dang-ki")
    public String getRegister(Model model){
        return "register";
    }
    @GetMapping("/dang-ki-with-gg")
    public String getRegisterwithgg(Model model){
        return "register-social";
    }
    @GetMapping("/forgot")
    public String getForgot(Model model){
        return "forgot";
    }
    @GetMapping("/resetpassword")
    public String getResetpassword(Model model){
        return "reset-password";
    }
}
