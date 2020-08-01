package com.example.fptufindingmotelv1.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
public class PostModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private TypeModel type;

    @ManyToOne
    @JoinColumn(name = "LANDLORD_ID", nullable = false)
    private LandlordModel landlord;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "DISTANCE", nullable = false)
    private double distance;

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
    private boolean visible;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "IS_BANNED", nullable = false)
    private boolean banned;

    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @Column(name = "MAP_LOCATION")
    private String mapLocation;

    @OneToMany(mappedBy = "post")
    private List<ImageModel> images;

    @OneToMany( mappedBy = "postReport")
    private List<ReportModel> reports;

    @OneToMany(mappedBy = "wishListPost")
    private List<WishListModel> wishLists;

    @OneToMany(mappedBy = "postPayment")
    private List<PaymentPostModel> paymentPosts;

    @OneToMany(mappedBy = "postRoom")
    private List<RoomModel> rooms;

}
