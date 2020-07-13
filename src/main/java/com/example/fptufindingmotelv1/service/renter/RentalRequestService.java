package com.example.fptufindingmotelv1.service.renter;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import net.minidev.json.JSONObject;

public interface RentalRequestService {
    JSONObject sentRentalRequest(RentalRequestDTO rentalRequestDTO);
    JSONObject changeStatus(String renterRequestId, Long statusId);
}
