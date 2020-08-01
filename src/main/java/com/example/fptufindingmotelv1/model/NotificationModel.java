package com.example.fptufindingmotelv1.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "NOTIFICATION")
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserModel userNotification;

    @Column(name = "[CONTENT]")
    private String content;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private StatusModel statusNotification;

    @ManyToOne
    @JoinColumn(name = "REQUEST_ID")
    private RentalRequestModel rentalRequestNotification;
}
