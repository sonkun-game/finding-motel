package com.example.fptufindingmotelv1.service.renter.report;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;

public interface ReportPostService {
    void sendReport(ReportRequestDTO reportRequestDTO);
}
