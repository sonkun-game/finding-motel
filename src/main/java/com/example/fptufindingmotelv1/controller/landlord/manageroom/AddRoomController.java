package com.example.fptufindingmotelv1.controller.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.landlord.manageroom.AddRoomService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AddRoomController {

    @Autowired
    AddRoomService addRoomService;

    @ResponseBody
    @PostMapping("/api-increase-room")
    public JSONObject increaseRoom(@RequestBody PostRequestDTO postRequestDTO) {
        List<RoomModel> roomModels = addRoomService.increaseRoom(postRequestDTO);

        return roomModels != null ?
                Constant.responseMsg("000", "Success", null) :
                Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
