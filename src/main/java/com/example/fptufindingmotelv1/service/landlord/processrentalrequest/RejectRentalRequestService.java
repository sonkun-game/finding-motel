package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;

public interface RejectRentalRequestService {

    boolean rejectRentalRequest(RentalRequestDTO rentalRequestDTO);

}
