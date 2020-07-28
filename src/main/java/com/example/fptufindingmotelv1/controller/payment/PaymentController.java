package com.example.fptufindingmotelv1.controller.payment;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.dto.PaymentPostDTO;
import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
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

    @ResponseBody
    @RequestMapping(value = "/api-get-history-payment-post")
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

    @RequestMapping(value = "/saveayment", method = RequestMethod.GET)
    public String sentNotify(Model model, @RequestParam(value = "errorCode") String errorCode, @RequestParam(value = "localMessage") String localMess) {
        return "redirect:/payment";
    }

    @ResponseBody
    @RequestMapping(value = "/get-momo-request", method = RequestMethod.POST)
    public MomoModel sentRequestMomo(@RequestBody String amountValue) {
        String partnerCode = "MOMO1J5T20200521";
        String accessKey = "Y09NiKaRm3Utzc6x";
        String requestType = "captureMoMoWallet";
        String requestId = createUniquieID();
        String amount = amountValue;
        String orderId = "lehuy113";
        String orderInfo = "Momo pay for user";
        String returnUrl = "http://localhost:8080/notifyPayStatus";
        String notifyUrl = "http://localhost:8080/notifyPayStatus";
        String extraData = "";
        String rawSign = "partnerCode=" + partnerCode + "&accessKey=" + accessKey
                + "&requestId=" + requestId + "&amount=" + amount + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo + "&returnUrl=" + returnUrl + "&notifyUrl=" + notifyUrl
                + "&extraData=" + extraData;

        String signature = getSignature(rawSign);
        MomoModel momoModel = new MomoModel(partnerCode, accessKey, requestType, requestId, amount, orderId, orderInfo
                , returnUrl, notifyUrl, extraData, signature);
        return momoModel;
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
        return "ffm" + UUID.randomUUID().toString();
    }

}
