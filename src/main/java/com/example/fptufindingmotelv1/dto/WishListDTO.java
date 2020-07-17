package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
public class WishListDTO {
    private String id;
    private String renterUsername;
    private Date createdDate;
    private String displayCreatedDate;
    private PostDTO post;
    private String postId;

    public WishListDTO(WishListModel wishListModel) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = wishListModel.getId();
        this.renterUsername = wishListModel.getWishListRenter().getUsername();
        this.createdDate = wishListModel.getCreatedDate();
        this.displayCreatedDate = sdf.format(wishListModel.getCreatedDate());
        this.post = new PostDTO(wishListModel.getWishListPost());
    }

    public WishListDTO() {
    }
}
