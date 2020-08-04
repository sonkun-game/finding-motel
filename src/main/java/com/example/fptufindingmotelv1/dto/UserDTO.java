package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

    private String newPassword;

    private Integer reportNumber;

    private Boolean banAvailable;

    private boolean banned;

    private int requestNumber;

    private boolean havePassword;

    private Long roleId;

    public UserDTO(UserModel userModel) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        Date date = new Date();
        this.username = userModel.getUsername();
        this.role = userModel.getRole().getRoleName();
        this.roleId = userModel.getRole().getId();
        this.roleName = userModel.getRole().getDisplayName();
        this.fbAccount = userModel.getFbAccount();
        this.ggAccount = userModel.getGgAccount();
        this.phoneNumber = userModel.getPhoneNumber();
        this.displayName = userModel.getDisplayName();
        this.havePassword = userModel.getPassword() != null && !userModel.getPassword().isEmpty();
        if (userModel instanceof RenterModel) {
            this.genderDisplay = ((RenterModel) userModel).isGender() ? "Nam" : "Nữ";
            this.gender = ((RenterModel) userModel).isGender();
            this.career = ((RenterModel) userModel).getCareer();
            this.dob = ((RenterModel) userModel).getDob();
        } else if (userModel instanceof LandlordModel) {
            this.unBanDate = ((LandlordModel) userModel).getUnBanDate() == null ? null : sdf.format(((LandlordModel) userModel).getUnBanDate());
            this.amount = ((LandlordModel) userModel).getAmount();
            this.reportNumber = 0;
            for (PostModel post:
                 ((LandlordModel) userModel).getPosts()) {
                if(post != null && post.getReports() != null){
                    for (ReportModel report:
                            post.getReports()) {
                        if(report.getStatusReport().getId() == 3 || report.getStatusReport().getId() == 4){
                            this.reportNumber ++;
                        }
                    }
                }
            }
            if(((LandlordModel) userModel).getUnBanDate() != null && ((LandlordModel)userModel).getUnBanDate().after(new Timestamp(date.getTime()))){
                this.banned = true;
            }else {
                this.banned = false;
            }
        }
    }

    public UserDTO() {
    }
}
