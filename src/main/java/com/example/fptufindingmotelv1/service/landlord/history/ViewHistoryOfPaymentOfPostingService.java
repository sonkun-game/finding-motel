package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentPostModel;

import java.util.List;

public interface ViewHistoryOfPaymentOfPostingService {
    List<PaymentPostModel> getListPaymentPostOfLandlord(PaymentDTO paymentDTO);
}
