package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "IMAGE")
public class ImageModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name="ID",nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel post;

    @Column(name="URL",nullable = false)
    private String url;

}
