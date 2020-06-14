package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.dto.LoginDTO;
import com.example.fptufindingmotelv1.dto.LoginRequestDTO;
import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.RoleModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.repository.UserModelRepository;
import com.example.fptufindingmotelv1.service.login.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserModelRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @ResponseBody
    @PostMapping(value = "/api-login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken((CustomUserDetails)authentication.getPrincipal());
        CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setAccessToken(token);
        responseDTO.setLoginDTO(new LoginDTO(userDetails.getUserModel()));
        return responseDTO;
    }

    @ResponseBody
    @GetMapping("/api-test-renter")
    public String testRenter(){
        return "Renter access accepted";
    }

    @ResponseBody
    @PostMapping("/api-get-authentication")
    public String getAuthentication(){
        return "authentication successfully ";
    }

    @ResponseBody
    @GetMapping("/api-test-landlord")
    public String testLandlord(){
        return "Landlord access accepted";
    }

    @ResponseBody
        @GetMapping("/api-insert-user")
    public void insertUser() {
        // Khi chương trình chạy
        // Insert vào csdl một user.
        UserModel user = new UserModel();
        user.setUsername("truong");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setPhoneNumber("0929730706");
        user.setRole(roleRepository.getOne(1L));
        userRepository.save(user);
        System.out.println(user);
    }

}
