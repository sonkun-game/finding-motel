package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;

public interface SentReportService {
    void sendReport(ReportRequestDTO reportRequestDTO);
}
