package com.example.fptufindingmotelv1.controller.login;

import com.example.fptufindingmotelv1.dto.LoginRequestDTO;
import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.service.login.JwtTokenProvider;
import com.example.fptufindingmotelv1.service.login.LoginService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    LoginService loginService;

    @GetMapping("/dang-nhap")
    public String getLogin(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            return "redirect:/";
        }
        return "login";
    }

    @ResponseBody
    @PostMapping(value = "/api-login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        LoginResponseDTO responseDTO = loginService.validateUser(loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword());
        if(!responseDTO.getMsgCode().equals("login000")){
            return responseDTO;
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());
        CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
        responseDTO.setAccessToken(token);
        UserDTO userDTO = new UserDTO(userDetails.getUserModel());
        responseDTO.setUserDTO(userDTO);
        return responseDTO;
    }


    @GetMapping(value = "/facebook-login")
    public RedirectView facebookLogin(){
        RedirectView redirectView = new RedirectView();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            redirectView.setUrl("/");
            return redirectView;
        }
        String url = loginService.facebookLogin();
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
        String accessToken = loginService.getFacebookToken(code);
        return loginService.getResponseFbLogin(accessToken, request);
    }
    @ResponseBody
    @PostMapping(value = "/api-get-facebook-profile")
    public JSONObject getFacebookProfile(@RequestParam String accessToken, HttpServletRequest request) throws IOException {
        return loginService.getResponseFbLogin(accessToken, request);
    }

    @GetMapping(value = "/google-login")
    public RedirectView googleLogin(){
        RedirectView redirectView = new RedirectView();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            redirectView.setUrl("/");
            return redirectView;
        }
        String url = loginService.googleLogin();
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
        String accessToken = loginService.getGoogleToken(code);
        return loginService.getResponseGgLogin(accessToken, request);
    }
    @ResponseBody
    @PostMapping(value = "/api-get-google-profile")
    public JSONObject getGoogleProfile(@RequestParam String accessToken, HttpServletRequest request) throws IOException {
        return loginService.getResponseGgLogin(accessToken, request);
    }


}
