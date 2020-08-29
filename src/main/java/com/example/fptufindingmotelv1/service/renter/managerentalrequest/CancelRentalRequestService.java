package com.example.fptufindingmotelv1.service.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import net.minidev.json.JSONObject;

public interface CancelRentalRequestService {
    JSONObject changeStatus(RentalRequestDTO rentalRequestDTO);
}
