package com.example.fptufindingmotelv1.controller.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.service.manageaccount.ManageUserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ManageUserController {

    @Autowired
    private ManageUserService manageUserService;

    @GetMapping(value = {"/profile-user", "/quan-ly-tai-khoan"})
    public String getProfileUser(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel() instanceof LandlordModel){
                return "profile-landlord";
            }else if(userDetails.getUserModel() instanceof RenterModel){
                return "profile-renter";
            }else {
                return "profile-admin";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/api-save-user-info")
    public JSONObject saveUserInfo(@RequestBody UserDTO request){
        JSONObject response = new JSONObject();
        if(manageUserService.saveUserInfo(request)){
            response.put("msgCode", "user000");
            response.put("message", "Update user information successfully");
        }else {
            response.put("msgCode", "sys999");
            response.put("message", "System Error");
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/api-change-phone-number")
    public JSONObject savePhoneNumber(@RequestBody UserDTO request){
        JSONObject response = new JSONObject();
        if(manageUserService.savePhone(request)){
            response.put("msgCode", "user000");
            response.put("message", "Change phone successfully");
        }else {
            response.put("msgCode", "sys999");
            response.put("message", "System Error");
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/api-change-password")
    public JSONObject savePassword(@RequestBody UserDTO request){
        return manageUserService.savePassword(request);
    }
}
