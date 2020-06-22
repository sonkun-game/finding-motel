package com.example.fptufindingmotelv1.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "POST")

public class PostModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private TypeModel type;

    @ManyToOne
    @JoinColumn(name = "LANDLORD_ID", nullable = false)
    private LandlordModel landlord;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "DISTANCE", nullable = false)
    private  double distance;

    @Column(name = "SQUARE", nullable = false)
    private double square;

    @Column(name = "ROOM_NUMBER", nullable = false)
    private int roomNumber;

    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @Column(name = "EXPIRE_DATE", nullable = false)
    private Date expireDate;

    @Column(name = "IS_VISIBLE", nullable = false)
    private boolean isVisible;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<ImageModel> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postReport")
    private List<ReportModel> reports;

    @ManyToMany(mappedBy = "posts")
    private List<RenterModel> renters;

}
