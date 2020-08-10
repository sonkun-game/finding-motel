package com.example.fptufindingmotelv1.dto;

import lombok.Data;

@Data
public class MomoTransactionStatusRequestDTO {
    private String partnerCode;
    private String accessKey;
    private String requestId;
    private String orderId;
    private String requestType;
    private String signature;
}
