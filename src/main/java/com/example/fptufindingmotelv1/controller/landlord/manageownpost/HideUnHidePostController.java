package com.example.fptufindingmotelv1.controller.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.HideUnHidePostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HideUnHidePostController {

    @Autowired
    private HideUnHidePostService hideUnHidePostService;

    @ResponseBody
    @PostMapping("/api-change-post-status")
    public JSONObject changePostStatus(@RequestBody PostRequestDTO postRequestDTO) {
        PostModel postModel = hideUnHidePostService.changePostStatus(postRequestDTO);

        return postModel != null ?
                Constant.responseMsg("000", "Success", null) :
                Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }

}
