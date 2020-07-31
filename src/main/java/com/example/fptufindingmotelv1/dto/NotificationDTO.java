package com.example.fptufindingmotelv1.dto;

import com.example.fptufindingmotelv1.model.NotificationModel;
import com.example.fptufindingmotelv1.untils.Constant;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class NotificationDTO {
    private String id;
    private String content;
    private Date createdDate;
    private Long statusId;
    private String username;
    private String displayDate;
    private boolean seen;
    private String roomId;
    private String requestId;

    public NotificationDTO(NotificationModel notificationModel) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT);
        this.id = notificationModel.getId();
        this.content = notificationModel.getContent();
        this.createdDate = notificationModel.getCreatedDate();
        this.statusId = notificationModel.getStatusNotification().getId();
        this.username = notificationModel.getUserNotification().getUsername();
        this.displayDate = sdf.format(createdDate);
        this.seen = this.statusId == 13;
        this.requestId = notificationModel.getRentalRequestNotification().getId();
        this.roomId = notificationModel.getRentalRequestNotification().getRentalRoom().getId();
    }

    public NotificationDTO() {
    }
}
