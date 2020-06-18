package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
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

    @Column(name = "IS_VISIBLE  ", nullable = false)
    private boolean isVisible;

    @OneToMany(mappedBy = "post")
    private List<ImageModel> images;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "WISHLIST",
            joinColumns = @JoinColumn(name = "POST_ID"),
            inverseJoinColumns = @JoinColumn(name = "RENTER_ID")
    )
    private List<RenterModel> renters;
}
