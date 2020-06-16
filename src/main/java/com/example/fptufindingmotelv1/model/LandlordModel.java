package com.example.fptufindingmotelv1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "LANDLORD")
@EqualsAndHashCode(callSuper = false)
@Data
public class LandlordModel extends UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "AMOUNT")
    private float amount;
}
