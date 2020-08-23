package com.example.fptufindingmotelv1.service.admin.managereport;

import com.example.fptufindingmotelv1.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteReportServiceImpl implements DeleteReportService{
    @Autowired
    ReportRepository reportRepository;

    @Override
    public void deleteReport(String id) {
        reportRepository.deleteById(id);
    }
}
