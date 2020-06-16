package com.example.fptufindingmotelv1.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;

    private String role;

    private String fbAccount;

    private String ggAccount;

    private String phoneNumber;

    private String password;

    private String displayName;
}
