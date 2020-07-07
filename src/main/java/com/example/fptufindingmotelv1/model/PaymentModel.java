package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PAYMENT")
public class PaymentModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "PAY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    @Column(name = "MOMO_ID")
    private String momoId;

    @ManyToOne
    @JoinColumn(name = "LANDLORD_ID")
    private LandlordModel landlordModel;

}
