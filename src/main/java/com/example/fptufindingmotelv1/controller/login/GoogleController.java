package com.example.fptufindingmotelv1.controller.login;

import com.example.fptufindingmotelv1.model.GooglePojo;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.service.login.SocialLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class GoogleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SocialLoginService socialLoginService;

    @GetMapping(value = "/google-login")
    public RedirectView googleLogin(){
        RedirectView redirectView = new RedirectView();
        String url = socialLoginService.googleLogin();
        redirectView.setUrl(url);
        return redirectView;
    }

    @GetMapping(value = "/google")
    public String google(@RequestParam("code") String code) throws IOException {
        String accessToken = socialLoginService.getGoogleToken(code);
        return "redirect:/google-profile?accessToken="+accessToken;
    }

    @GetMapping(value = "/google-profile")
    public String googleProfile(@RequestParam String accessToken, HttpServletRequest request, Model model) throws IOException {
        GooglePojo googlePojo = socialLoginService.getGgUserInfo(accessToken);
        UserModel userModel = userRepository.findByGgAccount(googlePojo.getId());
        if(userModel == null){
            userModel = new UserModel();
            userModel.setGgAccount(googlePojo.getId());
            userModel.setDisplayName(googlePojo.getName());
            model.addAttribute("userModel", userModel);
            return "register-social";
        }else {
            UserDetails userDetails = socialLoginService.buildUser(userModel);
            UsernamePasswordAuthenticationToken authentication = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "redirect:/";
        }

    }
}
