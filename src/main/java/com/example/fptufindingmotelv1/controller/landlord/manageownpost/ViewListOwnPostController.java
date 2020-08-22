package com.example.fptufindingmotelv1.controller.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.ViewListOwnPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
public class ViewListOwnPostController {

    @Autowired
    ViewListOwnPostService viewListOwnPostService;

    @Autowired
    LandlordRepository landlordRepository;

    @GetMapping(value = {"/dang-tin", "/nap-tien"})
    public String getFunctionPage(){
        Date date = new Date();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel().getRole().getId() == 2){
                UserModel userModel = landlordRepository.getLandlordByUsername(userDetails.getUsername());
                if(((LandlordModel) userModel).getUnBanDate() == null){
                    return "profile-landlord";
                }else if(((LandlordModel) userModel).getUnBanDate() != null
                        && ((LandlordModel) userModel).getUnBanDate().after(new Timestamp(date.getTime()))){
                    return "redirect:/";
                }
            }else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/quan-ly-bai-dang"})
    public String getManagerPage(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel().getRole().getId() == 2){
                return "profile-landlord";
            }else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/api-view-list-post")
    public JSONObject viewListPost(@RequestBody PostRequestDTO postRequestDTO) {
        List<PostModel> listPost = viewListOwnPostService.getAllPost(postRequestDTO);
        return listPost != null ?
                Constant.responseMsg("000", "Success", listPost) :
                Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
