package com.example.fptufindingmotelv1.service.landlord.payintoaccount;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.VnpayRequestDTO;
import com.example.fptufindingmotelv1.dto.VnpayResponseDTO;
import net.minidev.json.JSONObject;

public interface PayIntoAccountService {
    JSONObject savePayment(MomoResponseDTO momoResponseDTO);
    JSONObject requestMomoPayment(String amount);
    JSONObject validateAndSaveMomoPayment(MomoResponseDTO momoResponseDTO);
    JSONObject validateAndSaveVnpayPayment(VnpayResponseDTO vnpayResponseDTO);

    JSONObject requestVnpayPayment(VnpayRequestDTO vnpayRequestDTO);
    JSONObject savePaymentVnpay(VnpayResponseDTO vnpayResponseDTO);
}
