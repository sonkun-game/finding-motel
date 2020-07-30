package com.example.fptufindingmotelv1.controller.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.dto.PaymentPostDTO;
import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import com.example.fptufindingmotelv1.service.payment.PaymentService;
import com.restfb.json.Json;
import com.restfb.json.JsonObject;
import com.restfb.types.Payment;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class PaymentController {
    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    PaymentService paymentService;

    @ResponseBody
    @RequestMapping(value = "/api-get-history-payment-post")
    public List<PaymentPostDTO> getHistoryPaymentPost() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            //Get username of landlord
            LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
            List<PaymentPostDTO> response = new ArrayList<>();
            for (int i = 0; i < landlordModel.getPosts().size(); i++) {
                for (int j = 0; j < landlordModel.getPosts().get(i).getPaymentPosts().size(); j++) {
                    //Add to list PaymentPostDTO
                    response.add(new PaymentPostDTO(landlordModel.getPosts().get(i).getPaymentPosts().get(j)));
                }
            }
            return response;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/save-payment")
    public JSONObject savePayment(@RequestParam(value = "partnerCode") String partnerCode,
                            @RequestParam("accessKey") String accessKey,
                            @RequestParam("requestId") String requestId,
                            @RequestParam("orderId") String orderId,
                            @RequestParam("signature") String signature,
                            @RequestParam("amount") String amount,
                            @RequestParam("transId") String transId,
                            @RequestParam("errorCode") String errorCode) {
        MomoResponseDTO momoResponseDTO = new MomoResponseDTO();
        momoResponseDTO.setPartnerCode(partnerCode);
        momoResponseDTO.setAccessKey(accessKey);
        momoResponseDTO.setRequestId(requestId);
        momoResponseDTO.setOrderId(orderId);
        momoResponseDTO.setErrorCode(errorCode);
        momoResponseDTO.setAmount(amount);
        momoResponseDTO.setTransId(transId);
        momoResponseDTO.setSignature(signature);
        return paymentService.savePayment(momoResponseDTO);
    }

    @ResponseBody
    @RequestMapping(value = "/get-momo-predata", method = RequestMethod.POST)
    public JSONObject sentRequestMomo(@RequestBody String amountValue) {
        String partnerCode = "MOMO1J5T20200521";
        String accessKey = "Y09NiKaRm3Utzc6x";
        String requestType = "captureMoMoWallet";
        String requestId = "request_" + createUniquieID();
        String amount = amountValue;
        String orderId = "order_" + createUniquieID();
        String orderInfo = "Momo pay for user";
        String returnUrl = "https://localhost:8081/save-payment";
        String notifyUrl = "https://localhost:8081/save-payment";
        String extraData = "";
        String rawSign = "partnerCode=" + partnerCode + "&accessKey=" + accessKey
                + "&requestId=" + requestId + "&amount=" + amount + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo + "&returnUrl=" + returnUrl + "&notifyUrl=" + notifyUrl
                + "&extraData=" + extraData;

        String signature = getSignature(rawSign);
        MomoModel momoModel = new MomoModel(partnerCode, accessKey, requestType, requestId, amount, orderId, orderInfo
                , returnUrl, notifyUrl, extraData, signature);
        JSONObject response = new JSONObject();
        response.put("code", "000");
        response.put("data", momoModel);
        return response;
    }

    public String getSignature(String rawSign) {
        String key = "krEnQQAOs9oQexSsmbCETozJyTR6Gni9";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            sha256_HMAC.init(new SecretKeySpec(key.getBytes(), "HmacSHA256"));
            byte[] result = sha256_HMAC.doFinal(rawSign.getBytes());
            return bytesToHex(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String createUniquieID() {
        return "ffm_" + UUID.randomUUID().toString();
    }

}
