package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.service.landlord.ManageRequestService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ManageRequestController {

    @Autowired
    private ManageRequestService manageRequestService;

    @ResponseBody
    @PostMapping("/api-view-list-request")
    public JSONObject viewListRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        JSONObject response = new JSONObject();
        List<RentalRequestDTO> rentalRequestDTOList = new ArrayList<>();
        List<RentalRequestModel> rentalRequestModels = manageRequestService.getListRequest(rentalRequestDTO);
        for (RentalRequestModel rentalRequest:
             rentalRequestModels) {
            rentalRequestDTOList.add(new RentalRequestDTO(rentalRequest));
        }
        response.put("msgCode", rentalRequestModels != null ? "request000" : "sys999");
        response.put("listRequest", rentalRequestDTOList);
        return response;
    }
}
