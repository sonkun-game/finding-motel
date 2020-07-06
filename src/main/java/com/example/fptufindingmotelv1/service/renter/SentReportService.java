package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.model.ReportModel;
import org.springframework.stereotype.Service;

public interface SentReportService {
    void sendReport(ReportRequestDTO reportRequestDTO);
}
