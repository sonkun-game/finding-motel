package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePaymentPackageStatusServiceImpl implements ChangePaymentPackageStatusService{
    @Autowired
    StatusRepository statusRepository;

    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    @Override
    public PaymentPackageModel changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel =
                    paymentPackageRepository.findById(paymentPackageDTO.getId()).get();
            paymentPackageModel.setAvailable(!paymentPackageModel.isAvailable());
            return paymentPackageRepository.save(paymentPackageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
