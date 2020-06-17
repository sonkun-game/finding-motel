package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.UserModel;
import org.hibernate.LazyInitializationException;
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
        UserModel userModel = new UserModel();
        userModel.setDisplayName("truong");
        model.addAttribute("userModel", userModel);

        return "register-social";
    }

    @ResponseBody
    @GetMapping("/renter")
    public Principal getPrincipal (Principal principal) throws LazyInitializationException {
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
    @GetMapping("/profile-renter")
    public String getProfileRenter(Model model){
        return "profile-renter";
    }
    @GetMapping("/profile-admin")
    public String getProfileAdmin(Model model){
        return "profile-admin";
    }
    @GetMapping("/post-detail")
    public String getPostDetail(Model model){
        return "post-detail";
    }
}
