package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDTO {
    private int index;
    private String roomId;
    private String roomName;
    private boolean availableRoom;
    private long statusId;
    private String displayStatus;
    private String postId;
    private String postTitle;
    private int requestNumber;
    private List<RentalRequestDTO> listRentalRequest;
    private boolean openCollapse;

    public RoomDTO() {
    }

    public RoomDTO(int index, RoomModel roomModel) {
        this.index = index;
        this.roomId = roomModel.getId();
        this.roomName = roomModel.getName();
        this.statusId = roomModel.getStatus().getId();
        this.displayStatus = roomModel.getStatus().getStatus();
        this.availableRoom = roomModel.getStatus().getId() == 1 ? true : false;
        this.postId = roomModel.getPostRoom().getId();
        this.postTitle = roomModel.getPostRoom().getTitle();
        this.listRentalRequest = new ArrayList<>();
        this.openCollapse = false;
    }
}
