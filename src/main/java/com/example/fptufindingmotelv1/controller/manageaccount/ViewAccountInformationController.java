package com.example.fptufindingmotelv1.controller.manageaccount;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.service.manageaccount.ViewAccountInformationService;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewAccountInformationController {

    @Autowired
    ViewAccountInformationService viewAccountInformationService;

    @GetMapping(value = {"/profile-user", "/quan-ly-tai-khoan"})
    public String getProfileUser(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel().getRole().getId() == Constant.LANDLORD_ID){
                return "profile-landlord";
            }else if(userDetails.getUserModel().getRole().getId() == Constant.RENTER_ID){
                return "profile-renter";
            }else {
                return "profile-admin";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/api-get-authentication")
    public LoginResponseDTO getUserInformation(){
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return viewAccountInformationService.getUserInformation(userDetails.getUserModel(), responseDTO);

        }
        responseDTO.setMsgCode("403");
        responseDTO.setMessage("Not Authentication");
        return responseDTO;
    }
}
