package com.example.fptufindingmotelv1.service.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Service
public class ViewListOwnRentalRequestModel implements ViewListOwnRentalRequestService {

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Override
    public JSONObject searchRentalRequest(RentalRequestDTO rentalRequestDTO, Pageable pageable) {
        try {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, -30);
            Date cancelDateExpire = c.getTime();
            rentalRequestRepository.removeRentalRequest(cancelDateExpire, 8L, rentalRequestDTO.getRenterUsername());
            Page<RentalRequestModel> renterModels = rentalRequestRepository.searchRentalRequest(
                    rentalRequestDTO.getId(), rentalRequestDTO.getRenterUsername(), rentalRequestDTO.getRoomId()
                    ,rentalRequestDTO.getStatusId(), pageable);

            JSONObject response = Constant.responseMsg("000", "Success", renterModels.getContent());
            response.put("pagination", Constant.paginationModel(renterModels));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "System Error!", null);
        }
    }

}
