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
}
