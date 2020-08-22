package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import net.minidev.json.JSONObject;

public interface ViewAccountInformationService {

    LoginResponseDTO getUserInformation(UserModel userModel, LoginResponseDTO responseDTO);
}
