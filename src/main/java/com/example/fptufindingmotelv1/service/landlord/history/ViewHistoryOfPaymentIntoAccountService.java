package com.example.fptufindingmotelv1.service.landlord.history;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;

import java.util.List;

public interface ViewHistoryOfPaymentIntoAccountService {
    List<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO);
}
