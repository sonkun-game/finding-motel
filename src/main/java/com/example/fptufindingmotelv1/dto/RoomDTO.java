package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.RoomModel;
import lombok.Data;

@Data
public class RoomDTO {
    private int index;
    private String roomName;
    private boolean availableRoom;
    private long statusId;
    private String displayStatus;

    public RoomDTO(int index, RoomModel roomModel) {
        this.index = index;
        this.roomName = roomModel.getName();
        this.statusId = roomModel.getStatus().getId();
        this.displayStatus = roomModel.getStatus().getStatus();
        this.availableRoom = roomModel.getStatus().getId() == 1 ? true : false;
    }
}
