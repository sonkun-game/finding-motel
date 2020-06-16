package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "LANDLORD")
public class LandlordModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name="ID",nullable = false)
    private Long id;
    @Column(name="AMOUNT",nullable = false)
    private double amount;
}
