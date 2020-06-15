package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "[USER]")
@Inheritance(strategy = InheritanceType.JOINED)
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_NAME", nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleModel role;

    @Column(name = "FB_ACCOUNT")
    private String fbAccount;

    @Column(name = "GG_ACCOUNT")
    private String ggAccount;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

}
