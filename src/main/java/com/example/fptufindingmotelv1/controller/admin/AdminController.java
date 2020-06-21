package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;
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

    @RequestMapping(value = "/admin-profile")
    public String adminProfile(Model model) {
        return "profile-admin";
    }

    @ResponseBody
    @RequestMapping(value = "/get-all-user", method = RequestMethod.GET)
    public ArrayList<UserModel> getAllUser(Model model) {
        ArrayList<UserModel> users = adminService.getListUser();
        model.addAttribute("listUser", users);
        return users;
    }

    @ResponseBody
    @RequestMapping(value = "/ban-landlord")
    public ArrayList<LandlordModel> banLandlord(@RequestParam(value = "username") String username, Model model) {
        ArrayList<LandlordModel> landlords = null;
        if (username != null && username.length() > 0) {
            landlords = adminService.banLandlord(username);
        }
        return landlords;
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public ArrayList<LandlordModel> unbanLandlord(@RequestParam(value = "username") String username, Model model) {
        ArrayList<LandlordModel> landlords = null;
        if (username != null && username.length() > 0) {
            landlords = adminService.unbanLandlord(username);
        }
        return landlords;
    }

    @ResponseBody
    @RequestMapping(value = "/get-report", method = RequestMethod.GET)
    public ArrayList<ReportResponseDTO> getReport() {
        ArrayList<ReportResponseDTO> reports = adminService.getListReport();
        return reports;
    }

    @ResponseBody
    @RequestMapping(value = "/delete-report")
    public void deleteReport(@RequestParam(value = "reportId") String reportId) {
        adminService.deleteReport(Long.parseLong(reportId));
        getReport();
    }

    @ResponseBody
    @RequestMapping(value = "/get-post")
    public ArrayList<PostResponseDTO> getPost() {
        ArrayList<PostResponseDTO> posts = adminService.getListPost();
        return posts;
    }

    @RequestMapping(value = "/delete-post")
    public void deletePost(@RequestParam(value = "postId") String postId) {
        adminService.deletePost(Long.parseLong(postId));
    }
}


