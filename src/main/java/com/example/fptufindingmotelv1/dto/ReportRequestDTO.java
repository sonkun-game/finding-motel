package com.example.fptufindingmotelv1.dto;

import lombok.Data;

@Data
public class ReportRequestDTO {
    private String renterId;
    private String postId;
    private String content;
    private String postTitle;
    private String landlordId;
    private Long statusReport;
}
