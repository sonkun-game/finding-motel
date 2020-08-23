package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ViewInformationOfRenterServiceImpl implements ViewInformationOfRenterService {

    @Autowired
    private RenterRepository renterRepository;


    @Override
    public RenterModel getRenter(RentalRequestDTO rentalRequestDTO) {
        try {
            return renterRepository.getRenterByUsername(rentalRequestDTO.getRenterUsername());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
