package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;

public interface RentalRequestService {
    Boolean sentRentalRequest(RentalRequestDTO rentalRequestDTO);
    Boolean changeStatus(String renterRequestId, Long statusId);
}
