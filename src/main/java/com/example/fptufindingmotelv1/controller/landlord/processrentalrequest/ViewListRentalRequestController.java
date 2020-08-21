package com.example.fptufindingmotelv1.controller.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.service.landlord.processrentalrequest.ViewListRentalRequestService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewListRentalRequestController {

    @Autowired
    ViewListRentalRequestService viewListRentalRequestService;

    @ResponseBody
    @PostMapping("/api-get-requests-by-room")
    public JSONObject getListRequestByRoom(@RequestBody RentalRequestDTO rentalRequestDTO) {
        List<RentalRequestModel> rentalRequestModels = viewListRentalRequestService.getListRequestByRoom(rentalRequestDTO);

        return rentalRequestModels != null
                ? Constant.responseMsg("000", "Success", rentalRequestModels)
                : Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
