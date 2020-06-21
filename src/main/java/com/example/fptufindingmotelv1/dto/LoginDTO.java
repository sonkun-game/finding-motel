package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.RoleModel;
import com.example.fptufindingmotelv1.model.UserModel;
import lombok.Data;

import javax.persistence.Column;

@Data
public class LoginDTO {
    private String username;

    private String role;

    private String fbAccount;

    private String ggAccount;

    private String phoneNumber;

    private String password;

    private String displayName;

    public LoginDTO(UserModel userModel) {
        this.username = userModel.getUsername();
        this.role = userModel.getRole().getRoleName();
        this.fbAccount = userModel.getFbAccount();
        this.ggAccount = userModel.getGgAccount();
        this.phoneNumber = userModel.getPhoneNumber();
        this.password = userModel.getPassword();
        this.displayName = userModel.getDisplayName();
    }

    public LoginDTO() {
    }
}
