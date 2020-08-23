package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.service.landlord.postaroom.PostARoomService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostARoomController {

    @Autowired
    PostARoomService postARoomService;

    @ResponseBody
    @PostMapping("/api-get-init-new-post")
    public JSONObject getInitNewPost(){
        JSONObject response = new JSONObject();
        List<PaymentPackageModel> paymentPackages = postARoomService.getListPaymentPackage(true);
        if(paymentPackages != null){
            response.put("listPaymentPackage", paymentPackages);
        }
        List<TypeModel> typePosts = postARoomService.getListTypePost();
        if(typePosts != null){
            response.put("listTypePost", typePosts);
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/api-add-new-post")
    public JSONObject addNewPost(@RequestBody PostRequestDTO postRequestDTO) {
        try {
            return postARoomService.saveNewPost(postRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
