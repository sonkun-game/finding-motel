package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @OneToMany(mappedBy = "userNotification")
    private List<NotificationModel> notifications;

    public UserModel() {
    }

    public UserModel(String username) {
        this.username = username;
    }

    public UserModel(String username, Long roleId, String roleName, String displayRole, String fbAccount,
                     String ggAccount, String phoneNumber, String displayName, String password) {
        this.username = username;
        this.role = new RoleModel(roleId, roleName, displayRole);
        this.fbAccount = fbAccount;
        this.ggAccount = ggAccount;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.password = password;
    }

}
