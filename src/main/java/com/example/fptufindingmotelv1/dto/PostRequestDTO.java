package com.example.fptufindingmotelv1.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostRequestDTO {
    private long postId;
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
    private boolean isVisible;
}
