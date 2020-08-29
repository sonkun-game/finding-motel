package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;

public interface AddMoneyToLandlordService {
    LandlordModel addMoneyForLandlord(PaymentDTO paymentDTO);
}
