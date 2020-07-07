package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.TypePostDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ManagePostController {

    @Autowired
    private ManagePostService managePostService;

    @ResponseBody
    @PostMapping("/api-get-init-new-post")
    public JSONObject getInitNewPost(){
        JSONObject response = new JSONObject();
        List<PaymentPackageModel> paymentPackages = managePostService.getListPaymentPackage();
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
        return response;
    }

    @ResponseBody
    @PostMapping("/api-edit-post")
    public JSONObject editPost(@RequestBody PostRequestDTO postRequestDTO) {
        JSONObject response = new JSONObject();
        PostModel postModel = managePostService.editPost(postRequestDTO);
        response.put("msgCode", postModel != null ? "post000" : "sys999");
        response.put("postId", postModel.getId());
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
        response.put("msgCode", postResponseDTOS != null && postResponseDTOS.size() > 0 ? "post000" : "sys999");
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
        boolean isSuccess = managePostService.extendTimeOfPost(postRequestDTO);

        response.put("msgCode", isSuccess ? "post000" : "sys999");
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
}
