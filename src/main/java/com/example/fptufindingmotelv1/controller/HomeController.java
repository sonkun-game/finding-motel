package com.example.fptufindingmotelv1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomepage(Model model) {
        return "index";
    }

    @GetMapping("/dang-nhap")
    public String getLogin(Model model) {
        return "login";
    }

    @GetMapping("/dang-ki-with-gg")
    public String getRegisterwithgg(Model model) {
        return "register-social";
    }

    @ResponseBody
    @GetMapping("/renter")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
