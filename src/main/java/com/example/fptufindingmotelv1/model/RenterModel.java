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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "WISHLIST",
            joinColumns = @JoinColumn(name = "RENTER_ID"),
            inverseJoinColumns = @JoinColumn(name = "POST_ID")
    )
    private List<PostModel> posts;

}
