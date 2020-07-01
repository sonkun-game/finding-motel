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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "DURATION")
    private int duration;

    @OneToMany(mappedBy = "paymentPackage")
    private List<PaymentPostModel> paymentPosts;
}
