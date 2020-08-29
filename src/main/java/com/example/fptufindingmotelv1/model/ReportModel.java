package com.example.fptufindingmotelv1.model;

import com.example.fptufindingmotelv1.dto.ReportRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REPORT")
public class ReportModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

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

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private StatusModel statusReport;

    public ReportModel(RenterModel renterModel, PostModel postModel, StatusModel statusModel, ReportRequestDTO reportRequestDTO) throws Exception {
        this.renterReport = renterModel;
        this.postReport = postModel;
        this.statusReport = statusModel;
        this.content = reportRequestDTO.getContent();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));

        String currentDate = dateFormat.format(new Date());
        Date date = dateFormat.parse(currentDate);
        this.reportDate = date;
    }
    public ReportModel(String id, String content, Date reportDate, String postId, String postTitle,
                       String landlordUsername, String renterUsername, Long statusId, String statusNm) {
        this.id = id;
        this.content = content;
        this.reportDate = reportDate;
        this.postReport = new PostModel();
        this.postReport.setId(postId);
        this.postReport.setTitle(postTitle);
        this.postReport.setLandlord(new LandlordModel(landlordUsername));
        this.renterReport = new RenterModel(renterUsername);
        this.statusReport = new StatusModel(statusId, statusNm);
    }
}
