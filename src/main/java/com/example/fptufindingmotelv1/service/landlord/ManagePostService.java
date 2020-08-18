package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import net.minidev.json.JSONObject;

import java.util.List;

public interface ManagePostService {

    List<PaymentPackageModel> getListPaymentPackage(Boolean available);
    List<TypeModel> getListTypePost();

    JSONObject saveNewPost(PostRequestDTO postRequestDTO);

    List<PostModel> getAllPost(PostRequestDTO postRequestDTO);

    PostModel changePostStatus(PostRequestDTO postRequestDTO);

    JSONObject extendTimeOfPost(PostRequestDTO postRequestDTO);

    JSONObject deletePost(PostRequestDTO postRequestDTO);

    PostModel editPost(PostRequestDTO postRequestDTO);

    List<RoomModel> increaseRoom(PostRequestDTO postRequestDTO);

    List<ImageModel> getListImageByPost(PostRequestDTO postRequestDTO);
}
