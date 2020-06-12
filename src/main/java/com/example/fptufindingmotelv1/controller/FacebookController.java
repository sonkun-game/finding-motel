package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserModelRepository;
import com.example.fptufindingmotelv1.service.login.SocialLoginService;
import com.restfb.types.User;
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
public class FacebookController {
    @Autowired
    private UserModelRepository userModelRepository;

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
        String accessToken = socialLoginService.getFacebookToken(code);
        return "redirect:/facebook-profile?accessToken="+accessToken;
    }

    @GetMapping(value = "/facebook-profile")
    public String facebookProfile(@RequestParam String accessToken, HttpServletRequest request, Model model) throws IOException {
        User fbUser = socialLoginService.getFbUserInfo(accessToken);
        UserModel userModel = userModelRepository.findByFbAccount(fbUser.getId());
        if(userModel == null){
            userModel = new UserModel();
            userModel.setFbAccount(fbUser.getId());
            userModel.setDisplayName(fbUser.getName());
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
