package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import net.minidev.json.JSONObject;

public interface ManageUserService {
    public boolean saveUserInfo(UserDTO request);

    public boolean savePhone(UserDTO request);

    JSONObject savePassword(UserDTO request);
}
