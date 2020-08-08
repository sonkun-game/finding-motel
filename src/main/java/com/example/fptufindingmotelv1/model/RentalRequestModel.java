package com.example.fptufindingmotelv1.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private StatusModel rentalStatus;

    @OneToMany(mappedBy = "rentalRequestNotification")
    private List<NotificationModel> notifications;
}
