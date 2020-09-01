package com.example.fptufindingmotelv1.controller.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.service.landlord.processrentalrequest.RejectRentalRequestService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RejectRentalRequestController {

    @Autowired
    RejectRentalRequestService rejectRentalRequestService;

    @ResponseBody
    @PostMapping("/api-reject-request")
    public JSONObject rejectRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        try {
            boolean isSuccess = rejectRentalRequestService.rejectRentalRequest(rentalRequestDTO);

            return isSuccess
                    ? Constant.responseMsg("000", "Success", null)
                    : Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
