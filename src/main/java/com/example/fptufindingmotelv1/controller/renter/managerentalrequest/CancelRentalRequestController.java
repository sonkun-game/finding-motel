package com.example.fptufindingmotelv1.controller.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.service.renter.managerentalrequest.CancelRentalRequestService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CancelRentalRequestController {

    @Autowired
    CancelRentalRequestService cancelRentalRequestService;

    @ResponseBody
    @RequestMapping(value = "/change-rental-request-status", method = RequestMethod.POST)
    public JSONObject changeRentalRequestStatus(@RequestBody RentalRequestDTO rentalRequestDTO) {
        return cancelRentalRequestService.changeStatus(rentalRequestDTO);
    }


}
