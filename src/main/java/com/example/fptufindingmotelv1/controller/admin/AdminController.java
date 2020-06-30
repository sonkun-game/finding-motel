package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/profile-admin")
    public String adminProfile(Model model) {
        return "profile-admin";
    }

    @ResponseBody
    @RequestMapping(value = "/get-all-user")
    public ArrayList<UserDTO> getAllUser() {
        return adminService.getListUser();
    }

    @ResponseBody
    @RequestMapping(value = "/get-user-by-id")
    public ArrayList<UserDTO> getUserById(@RequestParam(value = "username") String username) {
        return adminService.searchUserByUsernameOrDisplayName(username);
    }

    @ResponseBody
    @RequestMapping(value = "/ban-landlord")
    public ArrayList<UserDTO> banLandlord(@RequestParam(value = "username") String username) {
        if (username != null && username.length() > 0) {
            adminService.banLandlord(username);
        }
        return adminService.getListUser();
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public ArrayList<UserDTO> unbanLandlord(@RequestParam(value = "username") String username) {
        if (username != null && username.length() > 0) {
            adminService.unbanLandlord(username);
            getAllUser();
        }
        return adminService.getListUser();
    }

    @ResponseBody
    @RequestMapping(value = "/get-report")
    public ArrayList<ReportResponseDTO> getReport() {
        ArrayList<ReportResponseDTO> reports = adminService.getListReport();
        return reports;
    }

    @ResponseBody
    @RequestMapping(value = "/delete-report")
    public JSONObject deleteReport(@RequestParam(value = "reportId") String reportId) {
        try {
            adminService.deleteReport(Long.parseLong(reportId));
            return responseMsg("000", "Success!");
        } catch (Exception e) {
            return responseMsg("001", "System error!");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get-post")
    public ArrayList<PostResponseDTO> getPost() {
        ArrayList<PostResponseDTO> posts = adminService.getListPost();
        return posts;
    }

    @ResponseBody
    @RequestMapping(value = "/delete-post")
    public JSONObject deletePost(@RequestParam(value = "postId") String postId) {
        try {
            adminService.deletePost(Long.parseLong(postId));
            return responseMsg("000", "Success!");
        } catch (Exception e) {
            return responseMsg("001", "System error!");
        }
    }

    @RequestMapping(value = "/post-detail")
    public String getPostDetail(Model model) {
        return "post-detail";
    }

    public JSONObject responseMsg(String code, String message) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        return msg;
    }
}

