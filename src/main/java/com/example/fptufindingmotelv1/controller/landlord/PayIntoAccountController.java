package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.dto.VnpayRequestDTO;
import com.example.fptufindingmotelv1.dto.VnpayResponseDTO;
import com.example.fptufindingmotelv1.service.landlord.payintoaccount.PayIntoAccountService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @RequestMapping(value = "/api-request-vnpay-payment", method = RequestMethod.POST)
    public JSONObject sentVnpayRequest(@RequestBody VnpayRequestDTO vnpayRequestDTO) {
        return payIntoAccountService.requestVnpayPayment(vnpayRequestDTO);
    }

    @ResponseBody
    @RequestMapping(value = "/api-check-sum-vnpay-payment")
    public JSONObject checkSumVnpayPayment(@RequestParam String vnp_TmnCode, @RequestParam Long vnp_Amount
            , @RequestParam String vnp_BankCode, @RequestParam String vnp_BankTranNo, @RequestParam String vnp_CardType
            , @RequestParam String vnp_PayDate, @RequestParam String vnp_OrderInfo
            , @RequestParam String vnp_TransactionNo, @RequestParam String vnp_ResponseCode, @RequestParam String vnp_TxnRef
            , @RequestParam String vnp_SecureHashType, @RequestParam String vnp_SecureHash) {
        VnpayResponseDTO vnpayResponseDTO = new VnpayResponseDTO(vnp_TmnCode, vnp_Amount,vnp_BankCode,vnp_BankTranNo,vnp_CardType,vnp_PayDate,vnp_OrderInfo,vnp_TransactionNo,vnp_ResponseCode,vnp_TxnRef,vnp_SecureHashType,vnp_SecureHash);
        return payIntoAccountService.validateAndSaveVnpayPayment(vnpayResponseDTO);
    }
}
