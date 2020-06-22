package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.UserModel;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

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

    private String unBanDate;

    private Float amount;

    private boolean gender;

    private String genderDisplay;

    private String career;

    private Date dob;

    public UserDTO(UserModel userModel) {
        this.username = userModel.getUsername();
        this.role = userModel.getRole().getId().toString();
        this.roleName = userModel.getRole().getRoleName();
        this.fbAccount = userModel.getFbAccount();
        this.ggAccount = userModel.getGgAccount();
        this.phoneNumber = userModel.getPhoneNumber();
        if (userModel instanceof RenterModel) {
            this.genderDisplay = ((RenterModel) userModel).isGender() ? "Nam" : "Nữ";
            this.gender = ((RenterModel) userModel).isGender();
            this.career = ((RenterModel) userModel).getCareer();
            this.dob = ((RenterModel) userModel).getDob();
        } else if (userModel instanceof LandlordModel) {
            this.unBanDate = ((LandlordModel) userModel).getUnBanDate() == null ? null : ((LandlordModel) userModel).getUnBanDate().toString();
            this.amount = ((LandlordModel) userModel).getAmount();
        }
    }

    public UserDTO() {
    }
}
