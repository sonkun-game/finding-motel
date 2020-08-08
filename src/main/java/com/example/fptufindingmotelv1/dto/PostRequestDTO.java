package com.example.fptufindingmotelv1.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostRequestDTO {
    private String postId;
    private long typeId;
    private String title;
    private String description;
    private double price;
    private double distance;
    private double square;
    private int roomNumber;
    private String username;

    private List<RoomDTO> listRoom;
    private List<String> listImage;
    private long paymentPackageId;
    private Boolean isVisible;
    private String address;
    private String mapLocation;
}
