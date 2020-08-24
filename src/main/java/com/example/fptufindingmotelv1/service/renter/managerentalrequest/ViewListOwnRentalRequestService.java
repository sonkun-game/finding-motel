package com.example.fptufindingmotelv1.service.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface ViewListOwnRentalRequestService {
    JSONObject searchRentalRequest(RentalRequestDTO rentalRequestDTO, Pageable pageable);
}
