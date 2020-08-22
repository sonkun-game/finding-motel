package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;

public interface BanUnbanLandlordService {
    LandlordModel banLandlord(String username);

    LandlordModel unbanLandlord(String username);
}
