package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;

@Data
public class ReportResponseDTO {
    private String id;
    private String renterId;
    private String postId;
    private String landlordName;
    private String postTitle;
    private String content;
    private String reportDate;
    private long statusId;
    private String displayStatus;

    public ReportResponseDTO(ReportModel report) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = report.getId();
        this.renterId = report.getRenterReport().getUsername();
        this.postId = report.getPostReport().getId();
        this.content = report.getContent();
        this.reportDate = sdf.format(report.getReportDate());
        this.postTitle = report.getPostReport().getTitle();
        this.landlordName = report.getPostReport().getLandlord().getUsername();
        this.statusId = report.getStatusReport().getId();
        this.displayStatus = report.getStatusReport().getStatus();
    }
}
