package com.example.fptufindingmotelv1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LANDLORD")
@EqualsAndHashCode(callSuper = false)
@Data
public class LandlordModel extends UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private float amount;

    @Column(name = "UNBAN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date unBanDate;

    @OneToMany(mappedBy = "landlord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostModel> posts;

    @OneToMany(mappedBy = "landlordModel", cascade = CascadeType.ALL)
    private List<PaymentModel> paymentModels;

    public LandlordModel(String username) {
        super(username);
    }
    public LandlordModel(String username, float amount, Date unBanDate) {
        super(username);
        this.amount = amount;
        this.unBanDate = unBanDate;
    }

    public LandlordModel() {
    }
}
