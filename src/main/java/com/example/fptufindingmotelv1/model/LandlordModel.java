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

    @Transient
    private long reportNumber;

    @Transient
    private boolean banAvailable;

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

    public LandlordModel(String username, Long roleId, String roleName, String displayRole, String fbAccount,
                       String ggAccount, String phoneNumber, String displayName, String password,
                         float amount, Date unBanDate) {
        super(username, roleId, roleName, displayRole, fbAccount, ggAccount, phoneNumber, displayName, password);
        this.amount = amount;
        this.unBanDate = unBanDate;
    }
}
