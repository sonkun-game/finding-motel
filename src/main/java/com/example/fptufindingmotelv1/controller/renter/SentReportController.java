package com.example.fptufindingmotelv1.controller.renter;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.service.renter.SentReportService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SentReportController {

    @Autowired
    SentReportService sentReportService;

    @ResponseBody
    @RequestMapping(value = "/sent-report")
    public JSONObject sendReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        try {
            sentReportService.sendReport(reportRequestDTO);
            return responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return responseMsg("001", e.getMessage(), null);
        }
    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
