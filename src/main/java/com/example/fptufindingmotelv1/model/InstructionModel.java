package com.example.fptufindingmotelv1.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "INSTRUCTION")
public class InstructionModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ROLE_ID")
    private RoleModel roleModel;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    public InstructionModel() {
    }

    public InstructionModel(Long id, String title, String content, Long roleId, String roleName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.roleModel = new RoleModel();
        this.roleModel.setId(roleId);
        this.roleModel.setRoleName(roleName);
    }
}
