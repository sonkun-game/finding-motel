package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.service.landlord.history.ViewHistoryOfPaymentIntoAccountService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class ViewHistoryOfPaymentIntoAccountController {

    @Autowired
    ViewHistoryOfPaymentIntoAccountService viewHistoryOfPaymentIntoAccountService;

    @Autowired
    Environment env;

    @ResponseBody
    @RequestMapping(value = "/api-get-payment-by-landlord")
    public JSONObject getHistoryPayment(@RequestBody PaymentDTO paymentDTO, @RequestParam Optional<Integer> currentPage) {
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
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize, Sort.by("payDate").descending());
            Page<PaymentModel> paymentModelPages = viewHistoryOfPaymentIntoAccountService.getPaymentsByLandlord(paymentDTO, pageable);

            JSONObject response = Constant.responseMsg("000", "Success", paymentModelPages.getContent());
            response.put("pagination", Constant.paginationModel(paymentModelPages));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
