package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String type;
    private String landlord;
    private double price;
    private double distance;
    private double square;
    private int roomNUmber;
    private Date createDate;
    private String description;
    private Date expireDate;
    private boolean isVisible;
    private String title;
    private String color;
    private List<ImageModel> images;

    public PostDTO(PostModel postModel) {
        this.id = postModel.getId();
        this.type = postModel.getType().getName();
        this.landlord = postModel.getLandlord().getUsername();
        this.price = postModel.getPrice();
        this.distance = postModel.getDistance();
        this.square = postModel.getSquare();
        this.roomNUmber = postModel.getRoomNumber();
        this.createDate = postModel.getCreateDate();
        this.description = postModel.getDescription();
        this.expireDate = postModel.getExpireDate();
        this.isVisible = postModel.isVisible();
        this.title = postModel.getTitle();
        this.images = postModel.getImages();
    }
}
