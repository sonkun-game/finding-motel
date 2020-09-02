package com.example.fptufindingmotelv1.controller.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.service.admin.managepaymentpackage.ChangePaymentPackageStatusService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangePaymentPackageStatusController {

    @Autowired
    ChangePaymentPackageStatusService  changePaymentPackageStatusService;

    
    @ResponseBody
    @RequestMapping(value = "/api-change-status-package")
    public JSONObject changeStatusPackage(@RequestBody PaymentPackageDTO paymentPackageDTO) {
        try {
            boolean isSuccess = changePaymentPackageStatusService.changeStatusPaymentPackage(paymentPackageDTO);

            return isSuccess
                    ? Constant.responseMsg("000", "Success!", null)
                    : Constant.responseMsg("999", "Lỗi hệ thống!", null);
        } catch (Exception e) {
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
