package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.common.viewinformationofapost.ViewInformationOfAPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewInformationOfAPostController {
    @Autowired
    private ViewInformationOfAPostService viewInformationOfAPostService;

    @GetMapping("/post-detail")
    public String getPostDetail() {
        return "post-detail";
    }

    @ResponseBody
    @PostMapping(value = "/api-post-detail")
    public JSONObject viewPost(@PathParam("id") String id){
        try {
            PostModel postModel = viewInformationOfAPostService.getPostDetail(id);
            PostDTO post = new PostDTO(postModel);
            if(postModel == null){
                return Constant.responseMsg("999", "Lỗi hệ thống!", null);
            }
            else if(!postModel.isVisible()){
                return Constant.responseMsg("001", "Bài đăng đã bị ẩn bởi chủ trọ hoặc hết hạn hiển thị!", post);

            }else if(postModel.isBanned()){
                return Constant.responseMsg("001", "Bài đăng đã bị khóa!", post);
            }

            return Constant.responseMsg("000", "Success!", post);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }

    }

    @ResponseBody
    @PostMapping(value = "/api-get-list-room-of-post")
    public JSONObject getListRoomOfPost(@PathParam("postId") String postId){
        JSONObject response = new JSONObject();
        List<RoomDTO> roomDTOS = new ArrayList<>();
        List<RoomModel> listRoomOfPost = viewInformationOfAPostService.getListRoomOfPost(postId);
        for (RoomModel r:
                listRoomOfPost) {
            roomDTOS.add(new RoomDTO(r));
        }
        response.put("code", listRoomOfPost != null ? "000" : "999");
        response.put("data", roomDTOS);
        return response;
    }

}
