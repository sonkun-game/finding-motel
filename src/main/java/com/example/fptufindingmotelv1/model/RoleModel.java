package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "ROLE")
public class RoleModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ROLE_NAME", nullable = false)
    private String roleName;

    @Column(name = "DISPLAY_NAME")
    private String instructionDesc;

    @OneToMany(mappedBy = "role")
    private List<UserModel> users;
}
