package com.example.fptufindingmotelv1.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "STATUS")
public class StatusModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TYPE", nullable = false)
    private Long type;

    @JoinColumn(name = "STATUS")
    private String status;

    @OneToMany(mappedBy = "status")
    private List<RoomModel> rooms;

    @OneToMany(mappedBy = "statusReport")
    private List<ReportModel> reports;

    @OneToMany(mappedBy = "rentalStatus")
    private List<RentalRequestModel> rentalRequest;

    @OneToMany(mappedBy = "statusNotification")
    private List<NotificationModel> notifications;
}
