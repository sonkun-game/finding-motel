package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.service.common.viewrelatedposts.ViewRelatedPostsService;
import net.minidev.json.JSONObject;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    ImageRepository imageRepository;

    @ResponseBody
    @PostMapping(value = "/api-get-related-post")
    public JSONObject getRelatedPost(@RequestBody PostRequestDTO postRequestDTO){
        JSONObject response = new JSONObject();
        List<PostDTO> listPostDTO = new ArrayList<>();
        List<PostModel> listPostModel= viewRelatedPostsService.getRelatedPosts(postRequestDTO);
        PostDTO postDTO;
        for (PostModel post:
                listPostModel) {
            ImageModel imageModel = imageRepository.getImageById(post.getImages().get(0).getId());
            post.getImages().get(0).setFileContent(imageModel.getFileContent());
            post.getImages().get(0).setFileType(imageModel.getFileType());
            postDTO = new PostDTO();
            postDTO.setPostDTO(post);
            listPostDTO.add(postDTO);
        }
        response.put("code", listPostModel != null ? "000" : "999");
        response.put("listPost", listPostDTO);
        return response;
    }
}
