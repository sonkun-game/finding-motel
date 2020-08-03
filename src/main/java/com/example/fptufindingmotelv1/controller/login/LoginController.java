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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    @PostMapping(value = "/api-logout")
    public JSONObject logout(HttpServletRequest request, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            jsonObject.put("message", "Log out successfully");
            jsonObject.put("code", "msg001");
            return jsonObject;
        }else {
            jsonObject.put("message", "Authentication is not exist");
            jsonObject.put("code", "msg002");
            return jsonObject;
        }

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



}
