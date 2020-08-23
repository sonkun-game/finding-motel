package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;

import java.util.List;

public interface AddRoomService {

    List<RoomModel> increaseRoom(PostRequestDTO postRequestDTO);

}
