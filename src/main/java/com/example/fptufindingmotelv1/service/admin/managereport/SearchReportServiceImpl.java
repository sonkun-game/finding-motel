package com.example.fptufindingmotelv1.service.admin.managereport;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.repository.ReportRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SearchReportServiceImpl implements SearchReportService {
    @Autowired
    ReportRepository reportRepository;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    public JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }

    @Override
    public JSONObject searchReport(ReportRequestDTO reportRequestDTO, Pageable pageable) {
        try {
            Page<ReportModel> reportModels = reportRepository.searchReport(reportRequestDTO.getLandlordId(),
                    reportRequestDTO.getRenterId(), reportRequestDTO.getPostTitle(),
                    reportRequestDTO.getStatusReport(), pageable);
            ArrayList<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
            for (ReportModel report : reportModels.getContent()) {
                ReportResponseDTO responseDTO = new ReportResponseDTO(report);
                reportResponseDTOS.add(responseDTO);
            }
            JSONObject response = responseMsg("000", "Success", reportResponseDTOS);
            response.put("pagination", paginationModel(reportModels));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("777", "Lỗi dữ liệu.", null);
        }
    }
}
