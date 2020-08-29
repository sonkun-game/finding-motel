package com.example.fptufindingmotelv1.service.landlord.payintoaccount;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import net.minidev.json.JSONObject;

public interface PayIntoAccountService {
    JSONObject savePayment(MomoResponseDTO momoResponseDTO);
    JSONObject requestMomoPayment(String amount);
    JSONObject validateAndSaveMomoPayment(MomoResponseDTO momoResponseDTO);
}
