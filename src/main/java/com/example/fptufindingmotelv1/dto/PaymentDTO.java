package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class PaymentDTO {
    private String id;
    private float amount;
    private String payDate;
    private String momoId;
    private String landlord;
    private String paymentMethod;
    private String note;

    public PaymentDTO(PaymentModel paymentModel){
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = paymentModel.getId();
        this.amount = paymentModel.getAmount();
        this.payDate = sdf.format(paymentModel.getPayDate());
        this.momoId = paymentModel.getPaymentTransaction();
        this.landlord = paymentModel.getLandlordModel().getUsername();
        this.paymentMethod = paymentModel.getPaymentMethod();
        this.note = paymentModel.getNote();
    }

    public PaymentDTO() {
    }
}
