package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SentReportServiceImpl implements SentReportService {

    @Autowired
    ReportRepository reportRepository;
    @Autowired
    RenterRepository renterRepository;
    @Autowired
    PostRepository postRepository;

    @Override
    public void sendReport(ReportRequestDTO reportRequestDTO) {
        try {
            ReportModel reportModel = null;
            PostModel postModel = postRepository.getOne(Long.parseLong(reportRequestDTO.getPostId()));
            RenterModel renterModel = renterRepository.getOne(reportRequestDTO.getRenterId());
            reportModel = new ReportModel(renterModel, postModel, reportRequestDTO);
            reportRepository.save(reportModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
