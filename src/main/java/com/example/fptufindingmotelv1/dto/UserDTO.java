package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.UserModel;
import lombok.Data;

@Data
public class UserDTO {
    private String username;

    private String role;

    private String roleName;

    private String fbAccount;

    private String ggAccount;

    private String phoneNumber;

    private String password;

    private String displayName;

    public UserDTO(UserModel userModel) {
        this.username = userModel.getUsername();
        this.role = userModel.getRole().getId().toString();
        this.roleName = userModel.getRole().getRoleName();
        this.fbAccount = userModel.getFbAccount();
        this.ggAccount = userModel.getGgAccount();
        this.phoneNumber = userModel.getPhoneNumber();

    }
}
