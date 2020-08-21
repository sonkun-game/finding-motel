package com.example.fptufindingmotelv1.controller.login;

import com.example.fptufindingmotelv1.dto.LoginRequestDTO;
import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.service.login.JwtTokenProvider;
import com.example.fptufindingmotelv1.service.login.LoginService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @GetMapping("/dang-nhap")
    public String getLogin(Model model){
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
        if(userDetails.getUserModel() instanceof LandlordModel){
            int countRequest = rentalRequestRepository.getRequestNumber(userDetails.getUsername(), 7L);
            userDTO.setRequestNumber(countRequest);
        }
        responseDTO.setUserDTO(userDTO);
        return responseDTO;
    }

    @ResponseBody
    @PostMapping("/api-get-authentication")
    public LoginResponseDTO getAuthentication(){
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            UserModel userModel = userRepository.findByUsername(userDetails.getUsername());
            UserDTO userDTO = new UserDTO(userModel);
            if(userModel instanceof LandlordModel){
                int countRequest = rentalRequestRepository.getRequestNumber(userModel.getUsername(), 7L);
                userDTO.setRequestNumber(countRequest);
            }
            responseDTO.setUserDTO(userDTO);
        }
        return responseDTO;
    }


    @GetMapping(value = "/facebook-login")
    public RedirectView facebookLogin(){
        RedirectView redirectView = new RedirectView();
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
