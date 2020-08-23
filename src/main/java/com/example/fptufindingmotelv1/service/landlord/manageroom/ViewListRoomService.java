package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;

import java.util.List;

public interface ViewListRoomService {

    List<RoomModel> getListRoom(RentalRequestDTO rentalRequestDTO);
}
