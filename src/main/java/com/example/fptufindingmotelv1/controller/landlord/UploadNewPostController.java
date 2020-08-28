package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.service.landlord.postaroom.UploadNewPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UploadNewPostController {

    @Autowired
    UploadNewPostService uploadNewPostService;

    @ResponseBody
    @PostMapping("/api-get-init-new-post")
    public JSONObject getInitNewPost(){
        JSONObject response = new JSONObject();
        List<PaymentPackageModel> paymentPackages = uploadNewPostService.getListPaymentPackage(true);
        if(paymentPackages != null){
            response.put("listPaymentPackage", paymentPackages);
        }
        List<TypeModel> typePosts = uploadNewPostService.getListTypePost();
        if(typePosts != null){
            response.put("listTypePost", typePosts);
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/api-add-new-post")
    public JSONObject addNewPost(@RequestBody PostRequestDTO postRequestDTO) {
        try {
            return uploadNewPostService.saveNewPost(postRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
