package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;

import java.util.List;

public interface ManagePostService {

    List<PaymentPackageModel> getListPaymentPackage();
    List<TypeModel> getListTypePost();
}
