package com.example.fptufindingmotelv1.controller.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.service.renter.sendrentalrequest.SendRentalRequestService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendRentalRequestController {

    @Autowired
    SendRentalRequestService sendRentalRequestService;

    @ResponseBody
    @RequestMapping(value = "/sent-rental-request")
    public JSONObject sentRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        return sendRentalRequestService.sentRentalRequest(rentalRequestDTO);
    }
}
