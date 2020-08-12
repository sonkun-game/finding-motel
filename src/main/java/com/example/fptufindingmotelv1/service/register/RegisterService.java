package com.example.fptufindingmotelv1.service.register;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;

public interface RegisterService {

    public UserModel save(UserDTO userDTO);
}
