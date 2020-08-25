package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RoomModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ViewListRoomService {

    Page<RoomModel> getListRoomPaging(RentalRequestDTO rentalRequestDTO, Pageable pageable);

    List<RoomModel> getListRoom(RentalRequestDTO rentalRequestDTO);
}
