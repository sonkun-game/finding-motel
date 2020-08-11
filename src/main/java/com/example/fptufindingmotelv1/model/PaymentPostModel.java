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

    public PaymentPostModel() {
    }

    public PaymentPostModel(String id, Date payDate, Long paymentPackageId,
                            float amount, int duration, String packageName,
                            String postId, String postTitle) {
        this.id = id;
        this.payDate = payDate;
        this.paymentPackage = new PaymentPackageModel();
        this.paymentPackage.setId(paymentPackageId);
        this.paymentPackage.setAmount(amount);
        this.paymentPackage.setDuration(duration);
        this.paymentPackage.setPackageName(packageName);
        this.postPayment = new PostModel();
        this.postPayment.setId(postId);
        this.postPayment.setTitle(postTitle);
    }
}
