package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.LoginDTO;
import net.minidev.json.JSONObject;

public interface ManageUserService {
    public boolean saveUserInfo(LoginDTO request);

    public boolean savePhone(LoginDTO request);

    JSONObject savePassword(LoginDTO request);
}
