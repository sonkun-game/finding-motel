package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewListRoomServiceImpl implements ViewListRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomModel> getListRoom(RentalRequestDTO rentalRequestDTO) {
        try {
            return roomRepository.getRooms(rentalRequestDTO.getRoomId(), rentalRequestDTO.getPostId(), rentalRequestDTO.getStatusId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
