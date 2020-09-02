package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;

public interface ChangePaymentPackageStatusService {
    boolean changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO);
}
