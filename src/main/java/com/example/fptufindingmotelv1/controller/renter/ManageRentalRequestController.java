package com.example.fptufindingmotelv1.controller.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ManageRentalRequestController {


    @ResponseBody
    @RequestMapping(value = "/sent-rental-request", method = RequestMethod.POST)
    public JSONObject sentRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/sent-rental-request", method = RequestMethod.POST)
    public JSONObject changeRentalRequestStatus(@RequestParam(value = "rentalRequestId") String rentalRequestId
            , @RequestParam(value = "StatusId") Long statusId) {
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/search-retal-request")
    public JSONObject searchRentalRequest() {

    }
}
