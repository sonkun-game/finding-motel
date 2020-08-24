package com.example.fptufindingmotelv1.controller.admin.managereport;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.service.admin.managereport.SearchReportService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class SearchReportController {
    @Autowired
    Environment env;

    @Autowired
    SearchReportService searchReportService;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    @ResponseBody
    @PostMapping(value = "/api-get-init-admin")
    public JSONObject getInitAdmin() {
        return searchReportService.getInitAdminManager();
    }

    @ResponseBody
    @RequestMapping(value = "/search-report")
    public JSONObject searchReport(@RequestBody ReportRequestDTO reportRequestDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize, Sort.by("reportDate").descending());
            return searchReportService.searchReport(reportRequestDTO, pageable);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
