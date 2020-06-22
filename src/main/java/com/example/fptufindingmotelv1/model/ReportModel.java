package com.example.fptufindingmotelv1.model;

import com.restfb.types.Post;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REPORT")
public class ReportModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RENTER_ID")
    private RenterModel renterReport;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel postReport;

    @Column(name = "[CONTENT]")
    private String content;

    @Column(name = "REPORT_DATE")
    private Date reportDate;
}
