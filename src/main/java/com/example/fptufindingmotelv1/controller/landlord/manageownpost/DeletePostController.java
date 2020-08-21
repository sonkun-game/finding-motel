package com.example.fptufindingmotelv1.controller.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.DeletePostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeletePostController {

    @Autowired
    DeletePostService deletePostService;

    @ResponseBody
    @PostMapping("/api-delete-post")
    public JSONObject deletePost(@RequestBody PostRequestDTO postRequestDTO) {
        try {
            return deletePostService.deletePost(postRequestDTO);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
