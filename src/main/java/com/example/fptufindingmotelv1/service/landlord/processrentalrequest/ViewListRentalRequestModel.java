package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewListRentalRequestModel implements ViewListRentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Override
    public List<RentalRequestModel> getListRequestByRoom(RentalRequestDTO rentalRequestDTO) {
        try {
            return rentalRequestRepository.getRentalRequests(rentalRequestDTO.getId(), rentalRequestDTO.getRoomId(), rentalRequestDTO.getStatusId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
