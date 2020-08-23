package com.example.fptufindingmotelv1.service.landlord.payintoaccount;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface PayIntoAccountService {
    JSONObject savePayment(MomoResponseDTO momoResponseDTO);
    JSONObject requestMomoPayment(String amount);
    JSONObject validateAndSaveMomoPayment(MomoResponseDTO momoResponseDTO);
}
