package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageRequestServiceImpl implements ManageRequestService{

    @Autowired
    private LandlordRepository landlordRepository;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<RentalRequestModel> getListRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            if(rentalRequestDTO.getLandlordUsername() != null && !rentalRequestDTO.getLandlordUsername().isEmpty()){
                LandlordModel landlordModel = landlordRepository.findByUsername(rentalRequestDTO.getLandlordUsername());
                return rentalRequestRepository.getListRequest(landlordModel.getUsername());
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RentalRequestModel> acceptRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            StatusModel statusAccept = statusRepository.findByIdAndType(9, 3);
            StatusModel statusReject = statusRepository.findByIdAndType(10, 3);
            for (RentalRequestModel request:
                 roomModel.getRoomRentals()) {
                if(!request.getId().equals(rentalRequestModel.getId())){
                    request.setRentalStatus(statusReject);
                }else {
                    request.setRentalStatus(statusAccept);
                }
            }
            StatusModel statusCancel = statusRepository.findByIdAndType(8, 3);
            RenterModel renter = rentalRequestModel.getRentalRenter();
            for (RentalRequestModel request:
                 renter.getRenterRentals()) {
                if(!request.getId().equals(rentalRequestModel.getId())){
                    request.setRentalStatus(statusCancel);
                }
            }
            rentalRequestRepository.saveAll(renter.getRenterRentals());
            List<RentalRequestModel> response = rentalRequestRepository.saveAll(roomModel.getRoomRentals());
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RentalRequestModel rejectRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            StatusModel statusReject = statusRepository.findByIdAndType(10, 3);
            rentalRequestModel.setRentalStatus(statusReject);
            return rentalRequestRepository.save(rentalRequestModel);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
