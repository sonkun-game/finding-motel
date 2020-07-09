package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import com.restfb.json.Json;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
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
    public ArrayList<UserDTO> getUserByUsernameOrDisplayName(@RequestParam(value = "username") String username) {
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
            adminService.deleteReport(reportId);
            return responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return responseMsg("999", "System error!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get-post")
    public JSONObject getPost() {
        try {
            ArrayList<PostResponseDTO> posts = adminService.getListPost();
            return responseMsg("000", "Success!", posts);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delete-post")
    public JSONObject deletePost(@RequestParam(value = "postId") String postId) {
        try {
            adminService.deletePost(postId);
            return responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/search-post")
    public JSONObject searchPost(@RequestBody PostSearchDTO postSearchDTO) {
        try {
            ArrayList<PostResponseDTO> posts = adminService.searchPost(postSearchDTO);
            return posts != null
                    ? responseMsg("000", "Success!", posts)
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ban-post")
    public JSONObject banPost(@RequestParam String postId) {
        try {

//            return posts != null
//                    ? responseMsg("000", "Success!", posts)
//                    : responseMsg("001", "SYSTEM ERROR", null);
            return responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
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


