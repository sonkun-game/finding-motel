package com.example.fptufindingmotelv1.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "PAYMENT")
public class PaymentModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "PAY_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    @Column(name = "MOMO_ID")
    private String momoId;

    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    @Column(name = "NOTE")
    private String note;

    @ManyToOne
    @JoinColumn(name = "LANDLORD_ID")
    private LandlordModel landlordModel;

    public PaymentModel() {
    }
    public PaymentModel(String id, float amount, Date payDate, String momoId,
                        String paymentMethod, String note) {
        this.id = id;
        this.amount = amount;
        this.payDate = payDate;
        this.momoId = momoId;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }
}
