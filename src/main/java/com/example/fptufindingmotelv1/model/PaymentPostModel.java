package com.example.fptufindingmotelv1.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PAYMENT_POST")
public class PaymentPostModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

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
