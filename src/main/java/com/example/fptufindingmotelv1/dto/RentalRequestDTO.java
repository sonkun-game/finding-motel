package com.example.fptufindingmotelv1.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RentalRequestDTO {
    String id;
    String renterUsername;
    String roomId;
    Date requestDate;
    Long statusId;
    String postId;
}
