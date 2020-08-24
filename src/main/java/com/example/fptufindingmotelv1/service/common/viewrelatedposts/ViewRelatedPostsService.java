package com.example.fptufindingmotelv1.service.common.viewrelatedposts;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;

import java.util.List;

public interface ViewRelatedPostsService {
    List<PostModel> getRelatedPosts(PostRequestDTO postRequestDTO);

}
