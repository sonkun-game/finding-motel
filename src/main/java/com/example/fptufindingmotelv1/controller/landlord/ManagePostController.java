package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ManagePostController {

    @Autowired
    private ManagePostService managePostService;

    @GetMapping(value = {"/dang-tin", "/nap-tien"})
    public String getFucntionPage(Model model){
        Date date = new Date();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel() instanceof LandlordModel){
                if(((LandlordModel) userDetails.getUserModel()).getUnBanDate() == null){
                    return "profile-landlord";
                }else if(((LandlordModel) userDetails.getUserModel()).getUnBanDate() != null
                && ((LandlordModel) userDetails.getUserModel()).getUnBanDate().after(new Timestamp(date.getTime()))){
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
            if(userDetails.getUserModel() instanceof LandlordModel){
                return "profile-landlord";
            }else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/api-get-init-new-post")
    public JSONObject getInitNewPost(){
        JSONObject response = new JSONObject();
        List<PaymentPackageModel> paymentPackages = managePostService.getListPaymentPackage(true);
        if(paymentPackages != null){
            List<PaymentPackageDTO> paymentPackageDTOS = new ArrayList<>();
            for (PaymentPackageModel paymentPackage:
                    paymentPackages) {
                paymentPackageDTOS.add(new PaymentPackageDTO(paymentPackage));
            }
            response.put("listPaymentPackage", paymentPackageDTOS);
        }
        List<TypeModel> typePosts = managePostService.getListTypePost();
        if(typePosts != null){
            List<TypePostDTO> typePostDTOS = new ArrayList<>();
            for (TypeModel typeModel:
                    typePosts) {
                typePostDTOS.add(new TypePostDTO(typeModel));
            }
            response.put("listTypePost", typePostDTOS);
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/api-add-new-post")
    public JSONObject addNewPost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        PostModel postModel = managePostService.saveNewPost(postRequestDTO);
        response.put("msgCode", postModel != null ? "post000" : "sys999");
        response.put("postId", postModel.getId());
        response.put("userInfo", new UserDTO(postModel.getLandlord()));
        return response;
    }

    @ResponseBody
    @PostMapping("/api-edit-post")
    public JSONObject editPost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        PostModel postModel = managePostService.editPost(postRequestDTO);
        response.put("msgCode", postModel != null ? "post000" : "sys999");
        response.put("postId", postModel.getId());
        response.put("userInfo", new UserDTO(postModel.getLandlord()));
        return response;
    }

    @ResponseBody
    @PostMapping("/api-view-list-post")
    public JSONObject viewListPost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        List<PostModel> listPost = managePostService.getAllPost(postRequestDTO);
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        for (PostModel post:
             listPost) {
            postResponseDTOS.add(new PostResponseDTO(post));
        }
        response.put("msgCode", listPost != null ? "post000" : "sys999");
        response.put("listPost", postResponseDTOS);
        return response;
    }

    @ResponseBody
    @PostMapping("/api-change-post-status")
    public JSONObject changePostStatus(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        PostModel postModel = managePostService.changePostStatus(postRequestDTO);

        response.put("msgCode", postModel != null ? "post000" : "sys999");
        response.put("post", new PostResponseDTO(postModel));
        return response;
    }

    @ResponseBody
    @PostMapping("/api-extend-time-of-post")
    public JSONObject extendTimeOfPost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        PostModel postModel = managePostService.extendTimeOfPost(postRequestDTO);

        response.put("msgCode", postModel != null ? "post000" : "sys999");
        response.put("post", new PostResponseDTO(postModel));
        response.put("userInfo", new UserDTO(postModel.getLandlord()));
        return response;
    }

    @ResponseBody
    @PostMapping("/api-delete-post")
    public JSONObject deletePost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        boolean isSuccess = managePostService.deletePost(postRequestDTO);

        response.put("msgCode", isSuccess ? "post000" : "sys999");
        return response;
    }

    @ResponseBody
    @PostMapping("/api-increase-room")
    public JSONObject increaseRoom(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        List<RoomModel> roomModels = managePostService.increaseRoom(postRequestDTO);

        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (RoomModel room:
             roomModels) {
            roomDTOS.add(new RoomDTO(0, room));
        }


        response.put("msgCode", roomModels != null ? "post000" : "sys999");
        response.put("listNewRoom", roomDTOS);
        return response;
    }
}
