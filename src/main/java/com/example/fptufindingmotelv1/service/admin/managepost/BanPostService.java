package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;

public interface BanPostService {
    boolean banPost(RentalRequestDTO rentalRequestDTO);
}
