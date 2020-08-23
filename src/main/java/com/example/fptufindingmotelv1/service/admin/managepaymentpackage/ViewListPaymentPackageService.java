package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ViewListPaymentPackageService {

    Page<PaymentPackageModel> getListPaymentPackage(Pageable pageable);

}
