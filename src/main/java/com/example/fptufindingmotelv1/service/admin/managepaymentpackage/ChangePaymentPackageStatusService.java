package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import net.minidev.json.JSONObject;

public interface ChangePaymentPackageStatusService {
    JSONObject getInitAdminManager();

    PaymentPackageModel changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO);
}
