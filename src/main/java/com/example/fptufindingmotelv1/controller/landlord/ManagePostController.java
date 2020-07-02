package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.PostRequestDTO;
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
}
