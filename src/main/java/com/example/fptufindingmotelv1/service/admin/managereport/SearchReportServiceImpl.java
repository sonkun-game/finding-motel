package com.example.fptufindingmotelv1.service.admin.managereport;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.dto.StatusDTO;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.ReportRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchReportServiceImpl implements SearchReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    StatusRepository statusRepository;

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
    public JSONObject getInitAdminManager() {
        JSONObject response = new JSONObject();
        try {
            // get list status report
            List<StatusModel> listStatus = statusRepository.findAllByType(2);
            List<StatusDTO> listStatusReport = new ArrayList<>();
            for (StatusModel status :
                    listStatus) {
                listStatusReport.add(new StatusDTO(status));
            }
            response.put("code", "000");
            response.put("listStatusReport", listStatusReport);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống!");
            return response;
        }
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
