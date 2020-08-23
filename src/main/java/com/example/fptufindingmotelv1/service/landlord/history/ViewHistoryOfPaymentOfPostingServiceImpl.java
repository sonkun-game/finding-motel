package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.repository.PaymentPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ViewHistoryOfPaymentOfPostingServiceImpl implements ViewHistoryOfPaymentOfPostingService {

    @Autowired
    PaymentPostRepository paymentPostRepository;

    @Override
    public List<PaymentPostModel> getListPaymentPostOfLandlord(PaymentDTO paymentDTO) {
        try {
            List<PaymentPostModel> paymentPostModels =
                    paymentPostRepository.getPaymentPostByLandlord(paymentDTO.getLandlord());
            return paymentPostModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
