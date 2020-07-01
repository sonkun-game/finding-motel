package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PAYMENT_POST")
public class PaymentPostModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "PAY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel postPayment;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_PACKAGE_ID")
    private PaymentPackageModel paymentPackage;
}
