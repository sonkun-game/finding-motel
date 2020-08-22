package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RenterModel;

import java.util.List;

public interface ViewInformationOfRenterService {

    RenterModel getRenter(RentalRequestDTO rentalRequestDTO);
}
