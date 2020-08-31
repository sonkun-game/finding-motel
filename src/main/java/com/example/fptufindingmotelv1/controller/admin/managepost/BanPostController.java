package com.example.fptufindingmotelv1.controller.admin.managepost;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.service.admin.managepost.BanPostService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BanPostController {
    @Autowired
    BanPostService banPostService;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/ban-post")
    public JSONObject banPost(@RequestParam String postId) {
        try {
            PostModel postModel = banPostService.banPost(postId);
            return postModel != null
                    ? responseMsg("000", "Success!", null)
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
