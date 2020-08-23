package com.example.fptufindingmotelv1.service.admin.managereport;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface SearchReportService {
    JSONObject searchReport(ReportRequestDTO reportRequestDTO, Pageable pageable);
}
