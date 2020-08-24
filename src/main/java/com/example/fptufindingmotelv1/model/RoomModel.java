package com.example.fptufindingmotelv1.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "ROOM")
public class RoomModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "STATUS_ID")
    private StatusModel status;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostModel postRoom;

    @OneToMany(mappedBy = "rentalRoom")
    private List<RentalRequestModel> roomRentals;

    @Transient
    private long requestNumber;

    public RoomModel() {
    }

    public RoomModel(String id) {
        this.id = id;
    }

    public RoomModel(String id, String name, Long statusId) {
        this.id = id;
        this.name = name;
        this.status = new StatusModel();
        this.status.setId(statusId);
    }
    public RoomModel(String id, String name, String postId, String postTitle,
                     Long statusId, String statusNm, long requestNumber) {
        this.id = id;
        this.name = name;
        this.postRoom = new PostModel();
        this.postRoom.setId(postId);
        this.postRoom.setTitle(postTitle);
        this.status = new StatusModel();
        this.status.setId(statusId);
        this.status.setStatus(statusNm);
        this.requestNumber = requestNumber;
    }

    public RoomModel(String id, String name, String postId, String postTitle) {
        this.id = id;
        this.name = name;
        this.postRoom = new PostModel();
        this.postRoom.setId(postId);
        this.postRoom.setTitle(postTitle);
    }
}
