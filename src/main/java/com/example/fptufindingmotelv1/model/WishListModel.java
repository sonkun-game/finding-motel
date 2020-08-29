package com.example.fptufindingmotelv1.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Data
@Table(name = "WISH_LIST")
public class WishListModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "RENTER_ID")
    private RenterModel wishListRenter;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel wishListPost;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public WishListModel() {
    }

    public WishListModel(String id, Date createdDate, String postId,
                         double price, double distance, double square,
                         String description, String postTitle, String address, String imageId) {
        this.id = id;
        this.createdDate = createdDate;
        this.wishListPost = new PostModel(postId, price, distance, square, description, postTitle, address, imageId);
    }
}
