package com.example.fptufindingmotelv1.controller.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.service.landlord.processrentalrequest.ViewInformationOfRenterService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewInformationOfRenterController {

    @Autowired
    ViewInformationOfRenterService viewInformationOfRenterService;

    @ResponseBody
    @PostMapping("/api-get-renter-info")
    public JSONObject getRenterInfo(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RenterModel renterModel = viewInformationOfRenterService.getRenter(rentalRequestDTO);

        return renterModel != null
                ? Constant.responseMsg("000", "Success", renterModel)
                : Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
