package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.service.landlord.payintoaccount.PayIntoAccountService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayIntoAccountController {

    @Autowired
    PayIntoAccountService payIntoAccountService;


    @RequestMapping(value = "/payment-momo")
    public String getReturnMomoPage() {
        return "profile-landlord";
    }

    @ResponseBody
    @RequestMapping(value = "/api-check-status-payment")
    public JSONObject checkStatusPayment(@RequestBody MomoResponseDTO momoResponseDTO) {
        return payIntoAccountService.validateAndSaveMomoPayment(momoResponseDTO);
    }


    @ResponseBody
    @RequestMapping(value = "/request-momo-payment", method = RequestMethod.POST)
    public JSONObject sentMomoRequest(@RequestBody String amountValue) {
        return payIntoAccountService.requestMomoPayment(amountValue);
    }
}
