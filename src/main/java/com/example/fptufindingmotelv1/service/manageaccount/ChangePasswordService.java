package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import net.minidev.json.JSONObject;

public interface ChangePasswordService {

    JSONObject savePassword(UserDTO request);
}
