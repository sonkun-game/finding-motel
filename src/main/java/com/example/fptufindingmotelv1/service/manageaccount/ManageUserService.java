package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.LoginDTO;

public interface ManageUserService {
    public boolean saveUserInfo(LoginDTO request);
}
