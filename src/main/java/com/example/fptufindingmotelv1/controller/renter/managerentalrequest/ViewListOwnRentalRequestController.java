package com.example.fptufindingmotelv1.controller.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.service.renter.managerentalrequest.ViewListOwnRentalRequestService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewListOwnRentalRequestController {

    @Autowired
    ViewListOwnRentalRequestService viewListOwnRentalRequestService;

    @ResponseBody
    @RequestMapping(value = "/search-rental-request")
    public JSONObject searchRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        return viewListOwnRentalRequestService.searchRentalRequest(rentalRequestDTO);
    }
}
