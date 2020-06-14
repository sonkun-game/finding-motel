package com.example.fptufindingmotelv1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

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
    
    @GetMapping("/dang-ki-with-gg")
    public String getRegisterWithGg(Model model){
        return "register-social";
    }

    @ResponseBody
    @GetMapping("/renter")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
    @GetMapping("/forgot")
    public String getForgot(Model model){
        return "forgot";
    }
    @GetMapping("/reset-password")
    public String getResetPassword(Model model){
        return "reset-password";
    }
    @GetMapping("/profile-landlord")
    public String getProfileLandlord(Model model){
        return "profile-landlord";
    }
}
