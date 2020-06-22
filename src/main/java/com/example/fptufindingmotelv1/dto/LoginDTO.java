package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoleModel;
import com.example.fptufindingmotelv1.model.UserModel;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class LoginDTO {
    private String username;

    private String role;

    private String fbAccount;

    private String ggAccount;

    private String phoneNumber;

    private String password;

    private String displayName;

    private float amount;

    private Date unBanDate;

    private boolean gender;

    private String career;

    private Date dob;

    private String newPassword;

    public LoginDTO(UserModel userModel) {
        this.username = userModel.getUsername();
        this.role = userModel.getRole().getRoleName();
        this.fbAccount = userModel.getFbAccount();
        this.ggAccount = userModel.getGgAccount();
        this.phoneNumber = userModel.getPhoneNumber();
        this.password = userModel.getPassword();
        this.displayName = userModel.getDisplayName();
        if(userModel instanceof LandlordModel){
            this.amount = ((LandlordModel) userModel).getAmount();
            this.unBanDate = ((LandlordModel) userModel).getUnBanDate();
        }else if(userModel instanceof RenterModel){
            this.career = ((RenterModel) userModel).getCareer();
            this.dob = ((RenterModel) userModel).getDob();
            this.gender = ((RenterModel) userModel).isGender();
        }
    }

    public LoginDTO() {
    }
}
