package com.example.fptufindingmotelv1.controller.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.landlord.manageroom.ChangeRoomStatusService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChangeRoomStatusController {

    @Autowired
    ChangeRoomStatusService changeRoomStatusService;

    @ResponseBody
    @PostMapping("/api-change-room-status")
    public JSONObject changeRoomStatus(@RequestBody RentalRequestDTO rentalRequestDTO) {
        RoomModel roomModel = changeRoomStatusService.changeRoomStatus(rentalRequestDTO);

        return roomModel != null
                ? Constant.responseMsg("000", "Success", null)
                : Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
