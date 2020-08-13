package com.example.fptufindingmotelv1.controller.viewdetailpost;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.viewdetailpost.ViewDetailService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewDetailController {
    @Autowired
    private ViewDetailService viewDetailService;

    @GetMapping("/post-detail")
    public String getPostDetail() {
        return "post-detail";
    }

    @ResponseBody
    @PostMapping(value = "/api-post-detail")
    public JSONObject viewPost(@PathParam("id") String id){
        try {
            PostModel postModel = viewDetailService.getPostDetail(id);
            PostDTO post = new PostDTO(postModel);
            if(postModel == null){
                return responseMsg("999", "Lỗi hệ thống!", null);
            }
            else if(!postModel.isVisible()){
                return responseMsg("001", "Bài đăng đã bị ẩn bởi chủ trọ hoặc hết hạn hiển thị!", post);

            }else if(postModel.isBanned()){
                return responseMsg("001", "Bài đăng đã bị khóa!", post);
            }

            return responseMsg("000", "Success!", post);
        }catch (Exception e){
            e.printStackTrace();
            return responseMsg("999", "Lỗi hệ thống!", null);
        }

    }

    @ResponseBody
    @PostMapping(value = "/api-get-related-post")
    public JSONObject getRelatedPost(@RequestBody PostRequestDTO postRequestDTO){
        JSONObject response = new JSONObject();
        List<PostResponseDTO> listPostDTO = new ArrayList<>();
        List<PostModel> listPostModel= viewDetailService.getRelatedPosts(postRequestDTO);
        for (PostModel post:
             listPostModel) {
            listPostDTO.add(new PostResponseDTO(post));
        }
        response.put("code", listPostModel != null ? "000" : "999");
        response.put("listPost", listPostDTO);
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/api-get-list-room-of-post")
    public JSONObject getListRoomOfPost(@PathParam("postId") String postId){
        JSONObject response = new JSONObject();
        List<RoomDTO> roomDTOS = new ArrayList<>();
        List<RoomModel> listRoomOfPost = viewDetailService.getListRoomOfPost(postId);
        for (RoomModel r:
                listRoomOfPost) {
            roomDTOS.add(new RoomDTO(r));
        }
        response.put("code", listRoomOfPost != null ? "000" : "999");
        response.put("data", roomDTOS);
        return response;
    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
