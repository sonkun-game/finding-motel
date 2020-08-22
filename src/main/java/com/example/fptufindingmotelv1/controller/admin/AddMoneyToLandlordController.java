package com.example.fptufindingmotelv1.controller.admin;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.service.admin.AddMoneyToLandlordService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddMoneyToLandlordController {

    @Autowired
    AddMoneyToLandlordService addMoneyForLandlordService;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    @ResponseBody
    @RequestMapping(value = "/api-add-money-for-landlord")
    public JSONObject addMoneyForLandlord(@RequestBody PaymentDTO paymentDTO) {
        try {
            LandlordModel landlordModel = addMoneyForLandlordService.addMoneyForLandlord(paymentDTO);

            return landlordModel != null
                    ? responseMsg("000", "Success!", new UserDTO(landlordModel))
                    : responseMsg("999", "Lỗi hệ thống. Nạp tiền không thành công!", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống. Nạp tiền không thành công!", null);
        }
    }
}
