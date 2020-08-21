package com.example.fptufindingmotelv1.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "RENTER")
public class RenterModel extends UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "GENDER")
    private boolean gender;

    @Column(name = "CAREER")
    private String career;

    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @OneToMany(mappedBy = "renterReport", cascade = CascadeType.ALL)
    private List<ReportModel> reports;

    @OneToMany(mappedBy = "wishListRenter")
    private List<WishListModel> wishListOfRenter;

    @OneToMany(mappedBy = "rentalRenter")
    private List<RentalRequestModel> renterRentals;

    public RenterModel(String username) {
        super(username);
    }

    public RenterModel(String username, Long roleId, String roleName, String displayRole, String fbAccount,
                       String ggAccount, String phoneNumber, String displayName, String password, boolean gender,
                       String career, Date dob) {
        super(username, roleId, roleName, displayRole, fbAccount, ggAccount, phoneNumber, displayName, password);
        this.gender = gender;
        this.career = career;
        this.dob = dob;
    }
}
