package com.example.fptufindingmotelv1.service.common.forgotpassword;

import com.example.fptufindingmotelv1.dto.UserDTO;

public interface ForgotPasswordService {
    public boolean saveNewPassword(UserDTO userDTO);
}
