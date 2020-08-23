package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavePaymentPackageServiceImpl implements SavePaymentPackageService{
    @Autowired
    PaymentPackageRepository paymentPackageRepository;
    @Override
    public PaymentPackageModel savePaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = new PaymentPackageModel();
            if (paymentPackageDTO.getId() != null) {
                paymentPackageModel =
                        paymentPackageRepository.findById(paymentPackageDTO.getId()).get();
            }
            paymentPackageModel.setPackageName(paymentPackageDTO.getPackageName());
            paymentPackageModel.setDuration(paymentPackageDTO.getDuration());
            paymentPackageModel.setAmount(paymentPackageDTO.getAmount());
            paymentPackageModel.setAvailable(true);
            return paymentPackageRepository.save(paymentPackageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
