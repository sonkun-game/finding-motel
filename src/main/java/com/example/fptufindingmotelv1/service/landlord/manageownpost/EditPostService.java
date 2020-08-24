package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;

import java.util.List;

public interface EditPostService {

    PostModel editPost(PostRequestDTO postRequestDTO);

    List<ImageModel> getListImageByPost(PostRequestDTO postRequestDTO);

}
