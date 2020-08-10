package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "FILTER_POST")
public class FilterPostModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "DISPLAY_TEXT")
    private String displayText;

    @Column(name = "MIN_VALUE")
    private Double minValue;

    @Column(name = "MAX_VALUE")
    private Double maxValue;
}
