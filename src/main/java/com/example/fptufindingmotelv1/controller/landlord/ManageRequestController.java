package com.example.fptufindingmotelv1.controller.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;
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
    @PostMapping("/api-accept-request")
    public JSONObject acceptRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        List<RentalRequestModel> rentalRequestModels = manageRequestService.acceptRentalRequest(rentalRequestDTO);

        return rentalRequestModels != null
                ? responseMsg("000", "Success", null)
                : responseMsg("999", "Lỗi hệ thống!", null);
    }

    @ResponseBody
    @PostMapping("/api-reject-request")
    public JSONObject rejectRentalRequest(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RentalRequestModel rentalRequestModel = manageRequestService.rejectRentalRequest(rentalRequestDTO);

        return rentalRequestModel != null
                ? responseMsg("000", "Success", null)
                : responseMsg("999", "Lỗi hệ thống!", null);
    }

    @ResponseBody
    @PostMapping("/api-change-room-status")
    public JSONObject changeRoomStatus(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RoomModel roomModel = manageRequestService.changeRoomStatus(rentalRequestDTO);

        return roomModel != null
                ? responseMsg("000", "Success", null)
                : responseMsg("999", "Lỗi hệ thống!", null);
    }

    @ResponseBody
    @PostMapping("/api-get-requests-by-room")
    public JSONObject getListRequestByRoom(@RequestBody RentalRequestDTO rentalRequestDTO) {
        List<RentalRequestModel> rentalRequestModels = manageRequestService.getListRequestByRoom(rentalRequestDTO);

        return rentalRequestModels != null
                ? responseMsg("000", "Success", rentalRequestModels)
                : responseMsg("999", "Lỗi hệ thống!", null);
    }

    @ResponseBody
    @PostMapping("/api-get-renter-info")
    public JSONObject getRenterInfo(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RenterModel renterModel = manageRequestService.getRenter(rentalRequestDTO);

        return renterModel != null
                ? responseMsg("000", "Success", renterModel)
                : responseMsg("999", "Lỗi hệ thống!", null);
    }
    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
