package com.example.fptufindingmotelv1.service.renter.report;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;

public interface ReportAPostService {
    void sendReport(ReportRequestDTO reportRequestDTO);
}
