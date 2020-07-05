package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentModel;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {
    private Long id;
    private float amount;
    private Date payDate;
    private String momoId;
    private String landlord;

    public PaymentDTO(PaymentModel paymentModel){
        this.id=paymentModel.getId();
        this.amount=paymentModel.getAmount();
        this.payDate=paymentModel.getPayDate();
        this.momoId=paymentModel.getMomoId();
        this.landlord=paymentModel.getLandlordModel().getUsername();
    }
}
