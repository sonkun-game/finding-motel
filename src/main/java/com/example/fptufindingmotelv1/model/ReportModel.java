package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "REPORT")
public class ReportModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "RENTER_ID")
    private String renterId;

    @Column(name = "POST_ID")
    private Long postID;

    @Column(name = "[CONTENT]")
    private Long content;

    @Column(name = "REPORT_DATE")
    private Date reportDate;
}
