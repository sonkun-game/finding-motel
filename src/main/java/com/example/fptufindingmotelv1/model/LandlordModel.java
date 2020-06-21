package com.example.fptufindingmotelv1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
}
