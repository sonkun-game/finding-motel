package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;

public interface LoginService {
    LoginResponseDTO validateUser(String username, String password);
}
