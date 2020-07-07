package com.example.fptufindingmotelv1.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "IMAGE")
public class ImageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel post;

    @Column(name="FILE_CONTENT",nullable = false)
    @Lob
    private byte[] fileContent;

    @Column(name="FILE_TYPE",nullable = false)
    private String fileType;
}
