package com.example.fptufindingmotelv1.dto;

import lombok.Data;

@Data
public class VnpayRequestDTO {
    private String vnpayContent;
    private String vnpayBank;
    private Long vnpayAmount;
}
