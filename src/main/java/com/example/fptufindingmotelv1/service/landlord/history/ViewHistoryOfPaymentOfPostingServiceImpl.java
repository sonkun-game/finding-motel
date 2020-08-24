package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.repository.PaymentPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ViewHistoryOfPaymentOfPostingServiceImpl implements ViewHistoryOfPaymentOfPostingService {

    @Autowired
    PaymentPostRepository paymentPostRepository;

    @Override
    public Page<PaymentPostModel> getListPaymentPostOfLandlord(PaymentDTO paymentDTO, Pageable pageable) {
        try {
            Page<PaymentPostModel> paymentPostModels =
                    paymentPostRepository.getPaymentPostByLandlord(paymentDTO.getLandlord(), pageable);
            return paymentPostModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
