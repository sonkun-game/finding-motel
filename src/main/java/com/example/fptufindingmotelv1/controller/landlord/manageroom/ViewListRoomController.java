package com.example.fptufindingmotelv1.controller.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.landlord.manageroom.ViewListRoomService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ViewListRoomController {

    @Autowired
    ViewListRoomService viewListRoomService;

    @ResponseBody
    @PostMapping("/api-get-rooms")
    public JSONObject getListRoom(@RequestBody RentalRequestDTO rentalRequestDTO) {
        List<RoomModel> roomModels = viewListRoomService.getListRoom(rentalRequestDTO);

        return roomModels != null
                ? Constant.responseMsg("000", "Success", roomModels)
                : Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }

}
