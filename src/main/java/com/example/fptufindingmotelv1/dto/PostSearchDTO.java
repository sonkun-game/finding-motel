package com.example.fptufindingmotelv1.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class PostSearchDTO {
    private String postId;
    private Long typeId;
    private String landlordUsername;
    private String title;
    private Double priceMax;
    private Double priceMin;
    private Double distanceMax;
    private Double distanceMin;
    private Double squareMax;
    private Double squareMin;
    private Boolean visible;
    private Long filterPriceId;
    private Long filterSquareId;
    private Long filterDistanceId;
    private Optional<Integer> page;
}
