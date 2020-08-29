package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ViewHistoryOfPaymentIntoAccountModel implements ViewHistoryOfPaymentIntoAccountService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public Page<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO, Pageable pageable) {
        try {
            Page<PaymentModel> paymentModels =
                    paymentRepository.getPaymentByLandlord(paymentDTO.getLandlord(), pageable);
            return paymentModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
