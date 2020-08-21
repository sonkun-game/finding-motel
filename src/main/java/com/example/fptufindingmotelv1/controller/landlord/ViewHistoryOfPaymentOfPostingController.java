package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.service.landlord.history.ViewHistoryOfPaymentOfPostingService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewHistoryOfPaymentOfPostingController {

    @Autowired
    ViewHistoryOfPaymentOfPostingService viewHistoryOfPaymentOfPostingService;

    @ResponseBody
    @PostMapping(value = "/api-get-history-payment-post")
    public JSONObject getHistoryPaymentPost(@RequestBody PaymentDTO paymentDTO){
        try {
            if(paymentDTO.getLandlord() == null && paymentDTO.getLandlord().isEmpty()){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth instanceof UsernamePasswordAuthenticationToken
                        && ((CustomUserDetails)auth.getPrincipal()).getUserModel() instanceof LandlordModel) {
                    paymentDTO.setLandlord(((CustomUserDetails) auth.getPrincipal()).getUsername());
                }else {
                    return Constant.responseMsg("403", "Access denied", null);
                }
            }
            List<PaymentPostModel> paymentPostModels = viewHistoryOfPaymentOfPostingService.getListPaymentPostOfLandlord(paymentDTO);

            return paymentPostModels != null
                    ? Constant.responseMsg("000", "Success!", paymentPostModels)
                    : Constant.responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
