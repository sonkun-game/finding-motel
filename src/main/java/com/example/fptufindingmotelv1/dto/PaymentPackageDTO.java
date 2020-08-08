package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import lombok.Data;

@Data
public class PaymentPackageDTO {
    private Long id;

    private float amount;

    private int duration;

    private String packageName;

    private boolean available;

    private String displayStatus;

    public PaymentPackageDTO(PaymentPackageModel paymentPackageModel) {
        this.id = paymentPackageModel.getId();
        this.amount = paymentPackageModel.getAmount();
        this.duration = paymentPackageModel.getDuration();
        this.packageName = paymentPackageModel.getPackageName();
        this.available = paymentPackageModel.isAvailable();
        this.displayStatus = this.available ? "Đang sử dụng" : "Không sử dụng";
    }

    public PaymentPackageDTO() {
    }
}
