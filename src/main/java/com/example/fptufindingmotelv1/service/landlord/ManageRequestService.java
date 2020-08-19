package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoomModel;

import java.util.List;

public interface ManageRequestService {

    List<RentalRequestModel> acceptRentalRequest(RentalRequestDTO rentalRequestDTO);

    RentalRequestModel rejectRentalRequest(RentalRequestDTO rentalRequestDTO);

    RoomModel changeRoomStatus(RentalRequestDTO rentalRequestDTO);

    List<RentalRequestModel> getListRequestByRoom(RentalRequestDTO rentalRequestDTO);

    RenterModel getRenter(RentalRequestDTO rentalRequestDTO);
}
