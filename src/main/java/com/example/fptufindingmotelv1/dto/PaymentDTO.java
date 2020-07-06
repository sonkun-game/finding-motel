package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class PaymentDTO {
    private Long id;
    private float amount;
    private String payDate;
    private String momoId;
    private String landlord;

    public PaymentDTO(PaymentModel paymentModel){
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id=paymentModel.getId();
        this.amount=paymentModel.getAmount();
        this.payDate = sdf.format(paymentModel.getPayDate());
        this.momoId=paymentModel.getMomoId();
        this.landlord=paymentModel.getLandlordModel().getUsername();
    }
}
