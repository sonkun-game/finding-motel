package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.HideUnHidePostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HideUnHidePostService hideUnHidePostService;

    @Autowired
    Environment env;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PaymentPackageRepository paymentPackageRepository;

    @RequestMapping(value = "/profile-admin")
    public String adminProfile(Model model) {
        return "profile-admin";
    }

    @GetMapping(value = {"/quan-ly-he-thong"})
    public String getManagerPage(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (!(userDetails.getUserModel().getRole().getId() == Constant.LANDLORD_ID)
                    && !(userDetails.getUserModel().getRole().getId() == Constant.RENTER_ID)) {
                return "profile-admin";
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-all-role")
    public JSONObject getAllRole() {
        try {
            List<RoleModel> roleModels = roleRepository.getAll();

            return roleModels != null
                    ? responseMsg("000", "Success!", roleModels)
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/get-post")
    public JSONObject getPost() {
        try {
            ArrayList<PostResponseDTO> posts = adminService.getListPost();
            return responseMsg("000", "Success!", posts);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

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
}


