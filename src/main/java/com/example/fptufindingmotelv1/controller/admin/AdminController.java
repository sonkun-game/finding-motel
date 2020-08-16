package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ManagePostService managePostService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/profile-admin")
    public String adminProfile(Model model) {
        return "profile-admin";
    }

    @GetMapping(value = {"/quan-ly-he-thong"})
    public String getManagerPage(Model model){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(!(userDetails.getUserModel() instanceof LandlordModel)
                && !(userDetails.getUserModel() instanceof RenterModel)){
                return "profile-admin";
            }else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @RequestMapping(value = "/api-search-user")
    public JSONObject searchUser(@RequestBody UserDTO userDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Pageable pageable = PageRequest.of(currentPage.orElse(0), 10);
            return adminService.searchUsers(userDTO, pageable);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
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
    @RequestMapping(value = "/ban-landlord")
    public JSONObject banLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && !username.isEmpty()) {
                LandlordModel landlordModel = adminService.banLandlord(username);
                return landlordModel != null
                        ? responseMsg("000", "Success!", null)
                        : responseMsg("999", "Lỗi hệ thống!", null);
            }
            return responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public JSONObject unbanLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && !username.isEmpty()) {
                LandlordModel landlordModel = adminService.unbanLandlord(username);
                return landlordModel != null
                        ? responseMsg("000", "Success!", null)
                        : responseMsg("999", "Lỗi hệ thống!", null);
            }

            return responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/delete-report")
    public JSONObject deleteReport(@RequestParam(value = "reportId") String reportId) {
        try {
            adminService.deleteReport(reportId);
            return responseMsg("000", "Success!", null);
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

    @ResponseBody
    @RequestMapping(value = "/search-post")
    public JSONObject searchPost(@RequestBody PostSearchDTO postSearchDTO) {
        try {
            ArrayList<PostResponseDTO> posts = adminService.searchPost(postSearchDTO);
            return posts != null
                    ? responseMsg("000", "Success!", posts)
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ban-post")
    public JSONObject banPost(@RequestParam String postId) {
        try {
            PostModel postModel = adminService.banPost(postId);
            return postModel != null
                    ? responseMsg("000", "Success!", new PostResponseDTO(postModel))
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            e.printStackTrace();
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

    @ResponseBody
    @RequestMapping(value = "/search-report")
    public JSONObject searchReport(@RequestBody ReportRequestDTO reportRequestDTO, @RequestParam Optional<Integer> currentPage ) {
        try {
            Pageable pageable = PageRequest.of(currentPage.orElse(0), 10, Sort.by("reportDate").descending());
            return adminService.searchReport(reportRequestDTO, pageable);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @PostMapping(value = "/api-get-init-admin")
    public JSONObject getInitAdmin() {
        return adminService.getInitAdminManager();
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-list-payment-package")
    public JSONObject getListPaymentPackage() {
        try {
            List<PaymentPackageModel> paymentPackageModels = managePostService.getListPaymentPackage(null);
            List<PaymentPackageDTO> response = new ArrayList<>();
            for (PaymentPackageModel paymentPackage:
                 paymentPackageModels) {
                response.add(new PaymentPackageDTO(paymentPackage));
            }
            return paymentPackageModels != null
                    ? responseMsg("000", "Success!", response)
                    : responseMsg("999", "Lỗi hệ thống", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/api-save-payment-package")
    public JSONObject savePaymentPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = adminService.savePaymentPackage(paymentPackageDTO);

            return paymentPackageModel != null
                    ? responseMsg("000", "Success!", new PaymentPackageDTO(paymentPackageModel))
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-change-status-package")
    public JSONObject changeStatusPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = adminService.changeStatusPaymentPackage(paymentPackageDTO);

            return paymentPackageModel != null
                    ? responseMsg("000", "Success!", new PaymentPackageDTO(paymentPackageModel))
                    : responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/api-add-money-for-landlord")
    public JSONObject addMoneyForLandlord(@RequestBody PaymentDTO paymentDTO) {
        try {
            LandlordModel landlordModel = adminService.addMoneyForLandlord(paymentDTO);

            return landlordModel != null
                    ? responseMsg("000", "Success!", new UserDTO(landlordModel))
                    : responseMsg("999", "Lỗi hệ thống. Nạp tiền không thành công!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống. Nạp tiền không thành công!", null);
        }
    }
}


