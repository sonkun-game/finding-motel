package com.example.fptufindingmotelv1.controller.login;

import com.example.fptufindingmotelv1.service.login.SocialLoginService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class SocialLoginController {

    @Autowired
    private SocialLoginService socialLoginService;

    @GetMapping(value = "/facebook-login")
    public RedirectView facebookLogin(){
        RedirectView redirectView = new RedirectView();
        String url = socialLoginService.facebookLogin();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/facebook")
    public String facebook(@RequestParam("code") String code) throws IOException {
        return "register-social";
    }

    @ResponseBody
    @PostMapping(value = "/api-get-facebook-login")
    public JSONObject getFacebookLogin(@RequestParam String code, HttpServletRequest request) throws IOException {
        String accessToken = socialLoginService.getFacebookToken(code);
        return socialLoginService.getResponseFbLogin(accessToken, request);
    }
    @ResponseBody
    @PostMapping(value = "/api-get-facebook-profile")
    public JSONObject getFacebookProfile(@RequestParam String accessToken, HttpServletRequest request) throws IOException {
        return socialLoginService.getResponseFbLogin(accessToken, request);
    }

    @GetMapping(value = "/google-login")
    public RedirectView googleLogin(){
        RedirectView redirectView = new RedirectView();
        String url = socialLoginService.googleLogin();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/google")
    public String google(@RequestParam("code") String code) throws IOException {
        return "register-social";
    }

    @ResponseBody
    @PostMapping(value = "/api-get-google-login")
    public JSONObject getGoogleLogin(@RequestParam String code, HttpServletRequest request) throws IOException {
        String accessToken = socialLoginService.getGoogleToken(code);
        return socialLoginService.getResponseGgLogin(accessToken, request);
    }
    @ResponseBody
    @PostMapping(value = "/api-get-google-profile")
    public JSONObject getGoogleProfile(@RequestParam String accessToken, HttpServletRequest request) throws IOException {
        return socialLoginService.getResponseGgLogin(accessToken, request);
    }
}
