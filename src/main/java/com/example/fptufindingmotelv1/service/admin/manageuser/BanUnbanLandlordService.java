package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;

public interface BanUnbanLandlordService {
    boolean banLandlord(String username);

    boolean unbanLandlord(String username);
}
