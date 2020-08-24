package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.service.landlord.history.ViewHistoryOfPaymentIntoAccountService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewHistoryOfPaymentIntoAccountController {

    @Autowired
    ViewHistoryOfPaymentIntoAccountService viewHistoryOfPaymentIntoAccountService;

    @ResponseBody
    @RequestMapping(value = "/api-get-payment-by-landlord")
    public JSONObject getHistoryPayment(@RequestBody PaymentDTO paymentDTO) {
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
            List<PaymentModel> paymentModels = viewHistoryOfPaymentIntoAccountService.getPaymentsByLandlord(paymentDTO);

            return paymentModels != null
                    ? Constant.responseMsg("000", "Success!", paymentModels)
                    : Constant.responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
