package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.service.common.viewrelatedposts.ViewRelatedPostsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewRelatedPostsController {

    @Autowired
    ViewRelatedPostsService viewRelatedPostsService;

    @ResponseBody
    @PostMapping(value = "/api-get-related-post")
    public JSONObject getRelatedPost(@RequestBody PostRequestDTO postRequestDTO){
        JSONObject response = new JSONObject();
        List<PostResponseDTO> listPostDTO = new ArrayList<>();
        List<PostModel> listPostModel= viewRelatedPostsService.getRelatedPosts(postRequestDTO);
        for (PostModel post:
                listPostModel) {
            listPostDTO.add(new PostResponseDTO(post));
        }
        response.put("code", listPostModel != null ? "000" : "999");
        response.put("listPost", listPostDTO);
        return response;
    }
}
