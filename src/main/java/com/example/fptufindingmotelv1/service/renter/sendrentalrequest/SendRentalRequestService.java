package com.example.fptufindingmotelv1.service.renter.sendrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import net.minidev.json.JSONObject;

public interface SendRentalRequestService {
    JSONObject sentRentalRequest(RentalRequestDTO rentalRequestDTO);
}
