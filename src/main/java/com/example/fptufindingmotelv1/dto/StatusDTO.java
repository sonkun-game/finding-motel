package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.StatusModel;
import lombok.Data;

@Data
public class StatusDTO {
    private long id;
    private long type;
    private String displayStatus;

    public StatusDTO(StatusModel statusModel) {
        this.id = statusModel.getId();
        this.type = statusModel.getType();
        this.displayStatus = statusModel.getStatus();
    }
}
