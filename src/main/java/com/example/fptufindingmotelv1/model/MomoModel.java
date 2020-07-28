package com.example.fptufindingmotelv1.model;

import lombok.Data;

@Data
public class MomoModel {
    private String partnerCode;
    private String accessKey;
    private String requestType;
    private String requestId;
    private String amount;
    private String orderId;
    private String orderInfo;
    private String returnUrl;
    private String notifyUrl;
    private String extraData;
    private String signature;

    public MomoModel(String partnerCode, String accessKey, String requestType, String requestId, String amount, String orderId, String orderInfo, String returnUrl, String notifyUrl, String extraData, String signature) {
        this.partnerCode = partnerCode;
        this.accessKey = accessKey;
        this.requestType = requestType;
        this.requestId = requestId;
        this.amount = amount;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.returnUrl = returnUrl;
        this.notifyUrl = notifyUrl;
        this.extraData = extraData;
        this.signature = signature;
    }
}
