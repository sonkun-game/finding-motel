package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import lombok.Data;

import java.util.Date;

@Data
public class RentalRequestDTO {
    private String id;
    private String renterUsername;
    private String roomId;
    private Date requestDate;
    private String displayDate;
    private Long statusId;
    private String statusDisplay;
    private String postId;
    private String landlordUsername;
    private String postTitle;
    private String roomName;

    public RentalRequestDTO() {
    }

    public RentalRequestDTO(RentalRequestModel rentalRequestModel) {
        this.id = rentalRequestModel.getId();
        this.renterUsername = rentalRequestModel.getRentalRenter().getUsername();
        this.roomId = rentalRequestModel.getRentalRoom().getId();
        this.requestDate = rentalRequestModel.getRequestDate();
        this.statusId = rentalRequestModel.getRentalStatus().getId();
        this.statusDisplay = rentalRequestModel.getRentalStatus().getStatus();
        this.postId = rentalRequestModel.getRentalRoom().getPostRoom().getId();
        this.landlordUsername = rentalRequestModel.getRentalRoom().getPostRoom().getLandlord().getUsername();
        this.postTitle = rentalRequestModel.getRentalRoom().getPostRoom().getTitle();
        this.roomName = rentalRequestModel.getRentalRoom().getName();
    }
}
