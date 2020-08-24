package com.example.fptufindingmotelv1.service.landlord.postaroom;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface PostARoomService {

    List<PaymentPackageModel> getListPaymentPackage(Boolean available);
    List<TypeModel> getListTypePost();

    JSONObject saveNewPost(PostRequestDTO postRequestDTO);
}
