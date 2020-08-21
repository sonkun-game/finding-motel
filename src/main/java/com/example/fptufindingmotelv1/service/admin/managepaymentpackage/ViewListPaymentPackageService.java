package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface ViewListPaymentPackageService {

    List<PaymentPackageModel> getListPaymentPackage(Boolean available);

}
