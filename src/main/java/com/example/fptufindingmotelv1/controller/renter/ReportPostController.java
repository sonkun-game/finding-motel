package com.example.fptufindingmotelv1.controller.renter;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import com.example.fptufindingmotelv1.service.renter.report.ReportPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReportPostController {

    @Autowired
    ReportPostService reportPostService;

    @ResponseBody
    @RequestMapping(value = "/sent-report")
    public JSONObject sendReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        try {
            reportPostService.sendReport(reportRequestDTO);
            return Constant.responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return Constant.responseMsg("001", e.getMessage(), null);
        }
    }

}
