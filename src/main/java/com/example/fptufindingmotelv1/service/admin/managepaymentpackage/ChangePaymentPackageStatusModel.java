package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePaymentPackageStatusModel implements ChangePaymentPackageStatusService{
    @Autowired
    StatusRepository statusRepository;

    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    @Override
    public boolean changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            paymentPackageRepository.updateAvailablePackage(paymentPackageDTO.getId(), paymentPackageDTO.isAvailable());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
