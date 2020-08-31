package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Data
public class PostDTO {
    private String id;
    private Long typeId;
    private String type;
    private String landlord;
    private String landlordDisplayName;
    private String phoneNumber;
    private double price;
    private double distance;
    private double square;
    private int roomNUmber;
    private String createDate;
    private String description;
    private boolean isVisible;
    private String title;
    private String color;
    private boolean inWishList;
    private List<String> images;
    private List<RoomDTO> listRoom;
    private String wishListId;
    private String address;
    private String mapLocation;
    private boolean outOfRoom;

    public PostDTO() {
    }

    public PostDTO(PostModel postModel) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.id = postModel.getId();
        this.typeId = postModel.getType().getId();
        this.type = postModel.getType().getName();
        this.landlord = postModel.getLandlord().getUsername();
        this.landlordDisplayName = postModel.getLandlord().getDisplayName();
        this.phoneNumber = postModel.getLandlord().getPhoneNumber();
        this.price = postModel.getPrice();
        this.distance = postModel.getDistance();
        this.square = postModel.getSquare();
        this.roomNUmber = postModel.getRoomNumber();
        this.createDate = sdf.format(postModel.getCreateDate());
        this.description = postModel.getDescription();
        this.isVisible = postModel.isVisible();
        this.title = postModel.getTitle();
        this.address = postModel.getAddress();
        this.mapLocation = postModel.getMapLocation();
        this.outOfRoom = postModel.isOutOfRoom();
        this.images = new ArrayList<>();
        for (ImageModel image:
                postModel.getImages()) {
            String imageUrl = "data:image/"+ image.getFileType()+";base64,"
                    + Base64.getEncoder().encodeToString(image.getFileContent());
            images.add(imageUrl);
        }
//        this.listRoom = new ArrayList<>();
//        for (int i = 0; i < postModel.getRooms().size(); i++) {
//            listRoom.add(new RoomDTO(i + 1, postModel.getRooms().get(i)));
//        }

    }
    public PostDTO(String id) {
        this.id = id;
    }
    public void setPostDTO(PostModel postModel){
        this.id = postModel.getId();
        this.price = postModel.getPrice();
        this.distance = postModel.getDistance();
        this.square = postModel.getSquare();
        this.description = postModel.getDescription();
        this.title = postModel.getTitle();
        this.address = postModel.getAddress();
        this.outOfRoom = postModel.isOutOfRoom();
        this.images = new ArrayList<>();
        String imageUrl = "data:image/"+ postModel.getImages().get(0).getFileType()+";base64,"
                + Base64.getEncoder().encodeToString(postModel.getImages().get(0).getFileContent());
        this.images.add(imageUrl);
    }

}
