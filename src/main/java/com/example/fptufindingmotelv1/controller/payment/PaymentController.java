package com.example.fptufindingmotelv1.controller.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.dto.PaymentPostDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import com.example.fptufindingmotelv1.service.payment.PaymentService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    PaymentService paymentService;

    @ResponseBody
    @PostMapping(value = "/api-get-history-payment")
    public List<PaymentDTO> getHistoryPaymentIntoAccount(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
            List<PaymentDTO> response = new ArrayList<>();
            for (PaymentModel payment: landlordModel.getPaymentModels()) {
                response.add(new PaymentDTO(payment));
            }
            return response;
        }
        return null;
    }

    @ResponseBody
    @PostMapping(value = "/api-get-history-payment-post")
    public List<PaymentPostDTO> getHistoryPaymentPost(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            //Get username of landlord
            LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
            List<PaymentPostDTO> response = new ArrayList<>();
            for(int i=0;i<landlordModel.getPosts().size();i++){
                for(int j=0;j<landlordModel.getPosts().get(i).getPaymentPosts().size();j++){
                    //Add to list PaymentPostDTO
                    response.add(new PaymentPostDTO(landlordModel.getPosts().get(i).getPaymentPosts().get(j)));
                }
            }
            return response;
        }
        return null;
    }


    @RequestMapping(value = "/validate-save-momo-payment")
    public String getMomoPaymentStatus(@RequestParam(value = "partnerCode") String partnerCode,
                                           @RequestParam("accessKey") String accessKey,
                                           @RequestParam("requestId") String requestId,
                                           @RequestParam("amount") String amount,
                                           @RequestParam("orderId") String orderId,
                                           @RequestParam("orderInfo") String orderInfo,
                                           @RequestParam("orderType") String orderType,
                                           @RequestParam("transId") String transId,
                                           @RequestParam("errorCode") String errorCode,
                                           @RequestParam("message") String message,
                                           @RequestParam("localMessage") String localMessage,
                                           @RequestParam("payType") String payType,
                                           @RequestParam("responseTime") String responseTime,
                                           @RequestParam("extraData") String extraData,
                                           @RequestParam("signature") String signature) {
        MomoResponseDTO momoResponseDTO = new MomoResponseDTO(partnerCode, accessKey, requestId, amount, orderId
                , orderInfo, orderType, transId, errorCode, message, localMessage, payType, responseTime, extraData, signature);
        JSONObject resp = paymentService.validateAndSaveMomoPayment(momoResponseDTO);
        return "profile-landlord";
    }


    @ResponseBody
    @RequestMapping(value = "/request-momo-payment", method = RequestMethod.POST)
    public JSONObject sentMomoRequest(@RequestBody String amountValue) {
        return paymentService.requestMomoPayment(amountValue);
    }
}
