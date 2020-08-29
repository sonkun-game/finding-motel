package com.example.fptufindingmotelv1.service.common.viewnotifications;

import com.example.fptufindingmotelv1.dto.NotificationDTO;
import com.example.fptufindingmotelv1.model.NotificationModel;

import java.util.List;

public interface ViewNotificationsService {
    List<NotificationModel> getListNotification(NotificationDTO notificationDTO);

    int getNotificationNumber(NotificationDTO request);

    NotificationModel changeStatusNotification(NotificationDTO request);
}
