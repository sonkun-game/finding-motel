package com.example.fptufindingmotelv1.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "POST")
public class PostModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "TYPE_ID")
    private TypeModel type;

    @ManyToOne
    @JoinColumn(name = "LANDLORD_ID")
    private LandlordModel landlord;

    @Column(name = "PRICE")
    private double price;

    @Column(name = "DISTANCE")
    private double distance;

    @Column(name = "SQUARE")
    private double square;

    @Column(name = "ROOM_NUMBER")
    private int roomNumber;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPIRE_DATE")
    private Date expireDate;

    @Column(name = "IS_VISIBLE")
    private boolean visible;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IS_BANNED")
    private boolean banned;

    @Column(name = "ADDRESS")
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

    public PostModel() {
    }

    public PostModel(String id) {
        this.id = id;
    }

    public PostModel(String id, Date expireDate) {
        this.id = id;
        this.expireDate = expireDate;
    }

    public PostModel(String id, double price, double distance, double square,
                     String description, String title, String address,
                     String imageId) {
        this.id = id;
        this.price = price;
        this.distance = distance;
        this.square = square;
        this.description = description;
        this.title = title;
        this.address = address;
        this.images = new ArrayList<>();
        this.images.add(new ImageModel(imageId));
    }

    public PostModel(String id, double price, double distance, double square, int roomNumber,
                     String description, String title, String address, boolean visible, boolean banned,
                     String mapLocation, Date createDate, Date expireDate, Long typeId, String typeName,
                     String landlordUsername, String landlordDisplayName, String landlordPhone
                     ) {
        this.id = id;
        this.price = price;
        this.distance = distance;
        this.square = square;
        this.roomNumber = roomNumber;
        this.description = description;
        this.title = title;
        this.address = address;
        this.visible = visible;
        this.banned = banned;
        this.mapLocation = mapLocation;
        this.createDate = createDate;
        this.expireDate = expireDate;
        this.type = new TypeModel();
        this.type.setId(typeId);
        this.type.setName(typeName);
        this.landlord = new LandlordModel();
        this.landlord.setUsername(landlordUsername);
        this.landlord.setDisplayName(landlordDisplayName);
        this.landlord.setPhoneNumber(landlordPhone);
    }
}
