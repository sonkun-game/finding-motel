package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.TypeModel;

import java.util.List;

public interface ManagePostService {

    List<PaymentPackageModel> getListPaymentPackage();
    List<TypeModel> getListTypePost();

    PostModel saveNewPost(PostRequestDTO postRequestDTO);

    List<PostModel> getAllPost(PostRequestDTO postRequestDTO);

    boolean changePostStatus(PostRequestDTO postRequestDTO);

    boolean extendTimeOfPost(PostRequestDTO postRequestDTO);

    boolean deletePost(PostRequestDTO postRequestDTO);
}
