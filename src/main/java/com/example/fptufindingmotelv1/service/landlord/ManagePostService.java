package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.model.TypeModel;

import java.util.List;

public interface ManagePostService {

    List<PaymentPackageModel> getListPaymentPackage(Boolean available);
    List<TypeModel> getListTypePost();

    PostModel saveNewPost(PostRequestDTO postRequestDTO);

    List<PostModel> getAllPost(PostRequestDTO postRequestDTO);

    PostModel changePostStatus(PostRequestDTO postRequestDTO);

    PostModel extendTimeOfPost(PostRequestDTO postRequestDTO);

    boolean deletePost(PostRequestDTO postRequestDTO);

    PostModel editPost(PostRequestDTO postRequestDTO);

    List<RoomModel> increaseRoom(PostRequestDTO postRequestDTO);
}
