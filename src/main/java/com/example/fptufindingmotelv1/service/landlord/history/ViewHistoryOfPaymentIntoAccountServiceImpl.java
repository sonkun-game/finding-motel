package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ViewHistoryOfPaymentIntoAccountServiceImpl implements ViewHistoryOfPaymentIntoAccountService {

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    public List<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO) {
        try {
            List<PaymentModel> paymentModels =
                    paymentRepository.getPaymentByLandlord(paymentDTO.getLandlord());
            return paymentModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
