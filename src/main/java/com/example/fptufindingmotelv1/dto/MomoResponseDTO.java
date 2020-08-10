package com.example.fptufindingmotelv1.dto;

import lombok.Data;

@Data
public class MomoResponseDTO {
    private String partnerCode;
    private String accessKey;
    private String requestId;
    private String amount;
    private String orderId;
    private String orderInfo;
    private String orderType;
    private String transId;
    private String errorCode;
    private String message;
    private String localMessage;
    private String payType;
    private String responseTime;
    private String extraData;
    private String signature;

    public MomoResponseDTO(String partnerCode, String accessKey, String requestId, String amount, String orderId
            , String orderInfo, String orderType, String transId, String errorCode, String message, String localMessage
            , String payType, String responseTime, String extraData, String signature) {
        this.partnerCode = partnerCode;
        this.accessKey = accessKey;
        this.requestId = requestId;
        this.amount = amount;
        this.orderId = orderId;
        this.orderInfo = orderInfo;
        this.orderType = orderType;
        this.transId = transId;
        this.errorCode = errorCode;
        this.message = message;
        this.localMessage = localMessage;
        this.payType = payType;
        this.responseTime = responseTime;
        this.extraData = extraData;
        this.signature = signature;
    }

    public MomoResponseDTO() {
    }
}
