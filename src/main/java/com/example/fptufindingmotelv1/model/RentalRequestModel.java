package com.example.fptufindingmotelv1.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "RENTAL_REQUEST")
public class RentalRequestModel implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "RENTER_ID")
    private RenterModel rentalRenter;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private RoomModel rentalRoom;

    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @Column(name = "START_DATE")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "CANCEL_DATE")
    @Temporal(TemporalType.DATE)
    private Date cancelDate;

    @Column(name = "EXPIRE_MESSAGE")
    private String expireMessage;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private StatusModel rentalStatus;

    @OneToMany(mappedBy = "rentalRequestNotification")
    private List<NotificationModel> notifications;

    public RentalRequestModel() {
    }

    public RentalRequestModel(String id) {
        this.id = id;
    }

    public RentalRequestModel(String id, String renterUsername) {
        this.id = id;
        this.rentalRenter = new RenterModel(renterUsername);
    }

    public RentalRequestModel(String id, String renterUsername, String roomId, String roomNm, Long statusId) {
        this.id = id;
        this.rentalRenter = new RenterModel(renterUsername);
        this.rentalRoom = new RoomModel(roomId, roomNm);
        this.rentalStatus = new StatusModel(statusId);
    }

    public RentalRequestModel(String id, Date requestDate, Date startDate, Date cancelDate, String expireMessage,
                              String renterUsername, Long statusId, String statusNm) {
        this.id = id;
        this.requestDate = requestDate;
        this.startDate = startDate;
        this.cancelDate = cancelDate;
        this.expireMessage = expireMessage;
        this.rentalRenter = new RenterModel(renterUsername);
        this.rentalStatus = new StatusModel(statusId, statusNm);
    }
    public RentalRequestModel(String id, Date requestDate, Date startDate, Date cancelDate, String expireMessage,
                              String postId, String postTitle, String landlordUsername, String roomId, String roomName,
                              Long statusId, String statusNm) {
        this.id = id;
        this.requestDate = requestDate;
        this.startDate = startDate;
        this.cancelDate = cancelDate;
        this.expireMessage = expireMessage;
        this.rentalStatus = new StatusModel(statusId, statusNm);
        this.rentalRoom = new RoomModel(roomId, roomName, postId, postTitle, landlordUsername);
    }
}
