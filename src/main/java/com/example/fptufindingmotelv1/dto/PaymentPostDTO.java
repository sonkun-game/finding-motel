package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class PaymentPostDTO {
    private String id;
    private String payDate;
    private String postId;
    private Long packageID;
    public PaymentPostDTO(PaymentPostModel paymentPostModel){
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = paymentPostModel.getId();
        this.payDate = sdf.format(paymentPostModel.getPayDate());
        this.postId = paymentPostModel.getPostPayment().getId();
        this.packageID = paymentPostModel.getPaymentPackage().getId();
    }
}
