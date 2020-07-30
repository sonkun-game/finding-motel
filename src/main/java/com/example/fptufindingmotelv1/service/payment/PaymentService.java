package com.example.fptufindingmotelv1.service.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import net.minidev.json.JSONObject;

public interface PaymentService {
    public JSONObject savePayment(MomoResponseDTO momoResponseDTO);
}
