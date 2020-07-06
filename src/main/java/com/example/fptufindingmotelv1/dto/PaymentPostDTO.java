package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import lombok.Data;

import java.util.Date;

@Data
public class PaymentPostDTO {
    private Long id;
    private Date payDate;
    private Long postId;
    private Long packageID;
    public PaymentPostDTO(PaymentPostModel paymentPostModel){
        this.id=paymentPostModel.getId();
        this.payDate=paymentPostModel.getPayDate();
        this.postId=paymentPostModel.getPostPayment().getId();
        this.packageID=paymentPostModel.getPaymentPackage().getId();
    }
}
