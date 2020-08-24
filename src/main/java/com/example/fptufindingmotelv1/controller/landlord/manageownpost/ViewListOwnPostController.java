package com.example.fptufindingmotelv1.controller.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.ViewListOwnPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Controller
public class ViewListOwnPostController {

    @Autowired
    ViewListOwnPostService viewListOwnPostService;

    @Autowired
    Environment env;

    @GetMapping(value = {"/dang-tin", "/nap-tien"})
    public String getFucntionPage() {
        Date date = new Date();
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (userDetails.getUserModel() instanceof LandlordModel) {
                if (((LandlordModel) userDetails.getUserModel()).getUnBanDate() == null) {
                    return "profile-landlord";
                } else if (((LandlordModel) userDetails.getUserModel()).getUnBanDate() != null
                        && ((LandlordModel) userDetails.getUserModel()).getUnBanDate().after(new Timestamp(date.getTime()))) {
                    return "redirect:/";
                }
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/quan-ly-bai-dang"})
    public String getManagerPage() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (userDetails.getUserModel() instanceof LandlordModel) {
                return "profile-landlord";
            } else {
                return "redirect:/";
            }
        }
        return "redirect:/";
    }

    @ResponseBody
    @PostMapping("/api-view-list-post")
    public JSONObject viewListPost(@RequestBody PostRequestDTO postRequestDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            Page<PostModel> listPostPage = viewListOwnPostService.getAllPost(postRequestDTO, pageable);
            JSONObject response = Constant.responseMsg("000", "Success", listPostPage.getContent());
            response.put("pagination", Constant.paginationModel(listPostPage));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
