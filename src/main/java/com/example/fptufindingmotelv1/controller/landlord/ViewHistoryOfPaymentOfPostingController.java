package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.service.landlord.history.ViewHistoryOfPaymentOfPostingService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class ViewHistoryOfPaymentOfPostingController {

    @Autowired
    ViewHistoryOfPaymentOfPostingService viewHistoryOfPaymentOfPostingService;

    @Autowired
    Environment env;
    @ResponseBody
    @PostMapping(value = "/api-get-history-payment-post")
    public JSONObject getHistoryPaymentPost(@RequestBody PaymentDTO paymentDTO, @RequestParam Optional<Integer> currentPage){
        try {
            if(paymentDTO.getLandlord() == null && paymentDTO.getLandlord().isEmpty()){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth instanceof UsernamePasswordAuthenticationToken
                        && ((CustomUserDetails)auth.getPrincipal()).getUserModel().getRole().getId() == Constant.LANDLORD_ID) {
                    paymentDTO.setLandlord(((CustomUserDetails) auth.getPrincipal()).getUsername());
                }else {
                    return Constant.responseMsg("403", "Access denied", null);
                }
            }
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            Page<PaymentPostModel> paymentPostModels = viewHistoryOfPaymentOfPostingService.getListPaymentPostOfLandlord(paymentDTO, pageable);
            JSONObject response = Constant.responseMsg("000", "Success", paymentPostModels.getContent());
            response.put("pagination", Constant.paginationModel(paymentPostModels));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }

    }
}
