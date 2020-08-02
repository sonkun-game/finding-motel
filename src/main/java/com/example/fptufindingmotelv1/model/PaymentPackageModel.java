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

    @OneToMany(mappedBy = "paymentPackage", cascade = CascadeType.ALL)
    private List<PaymentPostModel> paymentPosts;
}
