package com.example.fptufindingmotelv1.controller.viewdetailpost;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.service.viewdetailpost.ViewDetailService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewDetailController {
    @Autowired
    private ViewDetailService viewDetailService;

    @ResponseBody
    @PostMapping(value = "/api-get-related-post")
    public JSONObject getRelatedPost(@PathParam("id") String id){
        JSONObject response = new JSONObject();
        List<PostResponseDTO> listPostDTO = new ArrayList<>();
        List<PostModel> listPostModel= viewDetailService.getRelatedPosts(id);
        for (PostModel post:
             listPostModel) {
            listPostDTO.add(new PostResponseDTO(post));
        }
        response.put("msgCode", listPostModel != null ? "post000" : "sys999");
        response.put("listPost", listPostDTO);
        return response;
    }
}
