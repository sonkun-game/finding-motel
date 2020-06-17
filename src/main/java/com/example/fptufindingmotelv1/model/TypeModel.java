package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "Type")
public class TypeModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name="ID",nullable = false)
    private Long id;

    @Column(name="NAME",nullable = false)
    private String name;

    @OneToMany(mappedBy = "type")
    private List<PostModel> posts;
}
