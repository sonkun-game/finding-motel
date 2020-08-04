package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.service.admin.AdminService;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ManagePostService managePostService;

    @Autowired
    RoleRepository roleRepository;

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
    public JSONObject searchUser(@RequestBody UserDTO userDTO) {
        try {
            List<UserDTO> userDTOS = adminService.searchUsers(userDTO);
            return userDTOS != null
                    ? responseMsg("000", "Success!", userDTOS)
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-all-role")
    public JSONObject getAllRole() {
        try {
            List<RoleModel> roleModels = roleRepository.getAll();

            return roleModels != null
                    ? responseMsg("000", "Success!", roleModels)
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/ban-landlord")
    public JSONObject banLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && username.length() > 0) {
                adminService.banLandlord(username);
            }

            return responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public JSONObject unbanLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && username.length() > 0) {
                adminService.unbanLandlord(username);
            }

            return responseMsg("000", "Success!", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
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
            PostModel postModel = adminService.banPost(postId);
            return postModel != null
                    ? responseMsg("000", "Success!", new PostResponseDTO(postModel))
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-un-ban-post")
    public JSONObject unBanPost(@RequestParam String postId) {
        try {
            PostModel postModel = adminService.unBanPost(postId);
            return postModel != null
                    ? responseMsg("000", "Success!", new PostResponseDTO(postModel))
                    : responseMsg("001", "SYSTEM ERROR", null);
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

    @ResponseBody
    @RequestMapping(value = "/search-report")
    public JSONObject searchReport(@RequestBody ReportRequestDTO reportRequestDTO) {
        try {
            List<ReportResponseDTO> response = adminService.searchReport(reportRequestDTO);
            return response != null
                    ? responseMsg("000", "Success!", response)
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
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
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/api-save-payment-package")
    public JSONObject savePaymentPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = adminService.savePaymentPackage(paymentPackageDTO);

            return paymentPackageModel != null
                    ? responseMsg("000", "Success!", new PaymentPackageDTO(paymentPackageModel))
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/api-change-status-package")
    public JSONObject changeStatusPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = adminService.changeStatusPaymentPackage(paymentPackageDTO);

            return paymentPackageModel != null
                    ? responseMsg("000", "Success!", new PaymentPackageDTO(paymentPackageModel))
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }
    @ResponseBody
    @RequestMapping(value = "/api-add-money-for-landlord")
    public JSONObject addMoneyForLandlord(@RequestBody PaymentDTO paymentDTO) {
        try {
            LandlordModel landlordModel = adminService.addMoneyForLandlord(paymentDTO);

            return landlordModel != null
                    ? responseMsg("000", "Success!", new UserDTO(landlordModel))
                    : responseMsg("001", "SYSTEM ERROR", null);
        } catch (Exception e) {
            return responseMsg("999", e.getMessage(), null);
        }
    }
}


