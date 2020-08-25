package com.example.fptufindingmotelv1.controller.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.service.landlord.manageroom.ViewListRoomService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class ViewListRoomController {

    @Autowired
    ViewListRoomService viewListRoomService;

    @Autowired
    Environment env;

    @ResponseBody
    @PostMapping("/api-get-rooms")
    public JSONObject getListRoom(@RequestBody RentalRequestDTO rentalRequestDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            Page<RoomModel> roomModels = viewListRoomService.getListRoom(rentalRequestDTO, pageable);
            JSONObject response = Constant.responseMsg("000", "Success", roomModels.getContent());
            response.put("pagination", Constant.paginationModel(roomModels));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

}
