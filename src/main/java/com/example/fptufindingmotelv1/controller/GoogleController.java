package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.GooglePojo;
import com.example.fptufindingmotelv1.model.User;
import com.example.fptufindingmotelv1.service.GoogleService;
import com.example.fptufindingmotelv1.service.GoogleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
public class GoogleController {

    @Autowired
    private GoogleService googleService;

    @Autowired
    private GoogleUtils googleUtils;

    @GetMapping(value = "/google-login")
    public RedirectView googleLogin(){
        RedirectView redirectView = new RedirectView();
        String url = googleService.googleLogin();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/google")
    public String google(@RequestParam("code") String code) throws IOException {
        //String accessToken = googleService.getGoogleAccessToken(code);
        String accessToken = googleUtils.getToken(code);
        return "redirect:/google-profile?accessToken="+accessToken;
    }

    @GetMapping(value = "/google-profile")
    public String googleProfile(@RequestParam String accessToken, Model model) throws IOException {
//        Person person = googleService.getGoogleUserProfile(accessToken);
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        User user = new User();
        user.setGgAccount(googlePojo.getId());
        user.setDisplayName(googlePojo.getName());
        model.addAttribute("user", user);
        return "register-social";
    }
}
