package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ReportModel;
import lombok.Data;

@Data
public class ReportResponseDTO {
    private String id;
    private String renterId;
    private String postId;
    private String landlordName;
    private String postTitle;
    private String content;
    private String reportDate;

    public ReportResponseDTO(ReportModel report) {
        this.id = report.getId().toString();
        this.renterId = report.getRenterReport().getUsername();
        this.postId = report.getPostReport().getId().toString();
        this.content = report.getContent();
        this.reportDate = report.getReportDate().toString();
        this.postTitle = report.getPostReport().getTitle();
        this.landlordName = report.getPostReport().getLandlord().getUsername();
    }
}
