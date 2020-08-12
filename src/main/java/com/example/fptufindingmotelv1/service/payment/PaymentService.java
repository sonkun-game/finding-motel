package com.example.fptufindingmotelv1.service.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface PaymentService {
    JSONObject savePayment(MomoResponseDTO momoResponseDTO);
    JSONObject requestMomoPayment(String amount);
    JSONObject validateAndSaveMomoPayment(MomoResponseDTO momoResponseDTO);

    List<PaymentModel> getPaymentsByLandlord(PaymentDTO paymentDTO);
    List<PaymentPostModel> getListPaymentPostOfLandlord(PaymentDTO paymentDTO);
}
