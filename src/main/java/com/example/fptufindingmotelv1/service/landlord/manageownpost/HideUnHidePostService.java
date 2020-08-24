package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;

import java.util.List;

public interface HideUnHidePostService {

    List<PaymentPackageModel> getListPaymentPackage(Boolean available);

    PostModel changePostStatus(PostRequestDTO postRequestDTO);
}
