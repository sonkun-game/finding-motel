package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "PAYMENT_PACKAGE")
public class PaymentPackageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "DURATION")
    private int duration;

    @Column(name = "PACKAGE_NAME")
    private String packageName;

    @Column(name = "IS_AVAILABLE")
    private boolean available;

    @OneToMany(mappedBy = "paymentPackage", cascade = CascadeType.ALL)
    private List<PaymentPostModel> paymentPosts;

    public PaymentPackageModel() {
    }

    public PaymentPackageModel(Long id, float amount, int duration, String packageName, boolean available) {
        this.id = id;
        this.amount = amount;
        this.duration = duration;
        this.packageName = packageName;
        this.available = available;
    }
}
