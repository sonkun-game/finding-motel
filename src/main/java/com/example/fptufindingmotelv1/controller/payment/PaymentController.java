package com.example.fptufindingmotelv1.controller.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.PaymentPostDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import com.example.fptufindingmotelv1.service.payment.PaymentService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

//
//    @RequestMapping(value = "/validate-save-momo-payment")
//    public String getMomoPaymentStatus(@RequestParam(value = "partnerCode") String partnerCode,
//                                           @RequestParam("accessKey") String accessKey,
//                                           @RequestParam("requestId") String requestId,
//                                           @RequestParam("amount") String amount,
//                                           @RequestParam("orderId") String orderId,
//                                           @RequestParam("orderInfo") String orderInfo,
//                                           @RequestParam("orderType") String orderType,
//                                           @RequestParam("transId") String transId,
//                                           @RequestParam("errorCode") String errorCode,
//                                           @RequestParam("message") String message,
//                                           @RequestParam("localMessage") String localMessage,
//                                           @RequestParam("payType") String payType,
//                                           @RequestParam("responseTime") String responseTime,
//                                           @RequestParam("extraData") String extraData,
//                                           @RequestParam("signature") String signature) {
//        MomoResponseDTO momoResponseDTO = new MomoResponseDTO(partnerCode, accessKey, requestId, amount, orderId
//                , orderInfo, orderType, transId, errorCode, message, localMessage, payType, responseTime, extraData, signature);
//        JSONObject resp = paymentService.validateAndSaveMomoPayment(momoResponseDTO);
//        return "redirect:/nap-tien";
//    }

    @RequestMapping(value = "/payment-momo")
    public String getReturnMomoPage() {
        return "profile-landlord";
    }

    @ResponseBody
    @RequestMapping(value = "/api-check-status-payment")
    public JSONObject checkStatusPayment(@RequestBody MomoResponseDTO momoResponseDTO) {
        return paymentService.validateAndSaveMomoPayment(momoResponseDTO);
    }


    @ResponseBody
    @RequestMapping(value = "/request-momo-payment", method = RequestMethod.POST)
    public JSONObject sentMomoRequest(@RequestBody String amountValue) {
        return paymentService.requestMomoPayment(amountValue);
    }

    @ResponseBody
    @RequestMapping(value = "/api-get-payment-by-landlord")
    public JSONObject getPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            if(paymentDTO.getLandlord() == null && paymentDTO.getLandlord().isEmpty()){
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth instanceof UsernamePasswordAuthenticationToken
                        && ((CustomUserDetails)auth.getPrincipal()).getUserModel() instanceof LandlordModel) {
                    paymentDTO.setLandlord(((CustomUserDetails) auth.getPrincipal()).getUsername());
                }else {
                    return responseMsg("403", "Access denied", null);
                }
            }
            List<PaymentModel> paymentModels = paymentService.getPaymentsByLandlord(paymentDTO);

            return paymentModels != null
                    ? responseMsg("000", "Success!", paymentModels)
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
}
