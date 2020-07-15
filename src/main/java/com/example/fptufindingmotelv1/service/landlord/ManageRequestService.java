package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;

import java.util.List;

public interface ManageRequestService {
    List<RentalRequestModel> getListRequest(RentalRequestDTO rentalRequestDTO);

    List<RentalRequestModel> acceptRentalRequest(RentalRequestDTO rentalRequestDTO);

    RentalRequestModel rejectRentalRequest(RentalRequestDTO rentalRequestDTO);
}
