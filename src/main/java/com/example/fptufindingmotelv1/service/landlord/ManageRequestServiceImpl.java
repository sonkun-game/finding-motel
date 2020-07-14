package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageRequestServiceImpl implements ManageRequestService{

    @Autowired
    private LandlordRepository landlordRepository;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

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
}
