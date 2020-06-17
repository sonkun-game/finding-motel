package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @RequestMapping(value = "/ban-landlord", method = RequestMethod.GET)
    public ArrayList<LandlordModel> banLandlord(@RequestParam(value = "username") String username, Model model) {
        ArrayList<LandlordModel> landlords = null;
        if (username != null && username.length() > 0) {
            landlords = adminService.banLandlord(username);
        }
        return landlords;
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord", method = RequestMethod.GET)
    public ArrayList<LandlordModel> unbanLandlord(@RequestParam(value = "username") String username, Model model) {
        ArrayList<LandlordModel> landlords = null;
        if (username != null && username.length() > 0) {
            landlords = adminService.unbanLandlord(username);
        }
        return landlords;
    }

    @RequestMapping(value = "/get-report", method = RequestMethod.GET)
    public String unbanLandlord(Model model) {
        ArrayList<ReportModel> reports = adminService.getListReport();
        model.addAttribute("listReport", reports);
        return "";
    }
}
