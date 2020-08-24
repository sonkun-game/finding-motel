package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;

public interface ChangeUserInformationService {
    boolean saveUserInfo(UserDTO request);

}
