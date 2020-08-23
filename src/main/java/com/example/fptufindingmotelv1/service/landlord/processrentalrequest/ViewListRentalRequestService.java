package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;

import java.util.List;

public interface ViewListRentalRequestService {

    List<RentalRequestModel> getListRequestByRoom(RentalRequestDTO rentalRequestDTO);

}
