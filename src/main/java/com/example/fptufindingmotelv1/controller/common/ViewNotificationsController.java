package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.dto.NotificationDTO;
import com.example.fptufindingmotelv1.model.NotificationModel;
import com.example.fptufindingmotelv1.service.common.viewnotifications.ViewNotificationsService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ViewNotificationsController {

    @Autowired
    private ViewNotificationsService viewNotificationsService;

    @ResponseBody
    @PostMapping("/api-get-notifications")
    public JSONObject getNotifications(@RequestBody NotificationDTO request){
        JSONObject response = new JSONObject();
        if(request.getUsername() == null || request.getUsername().isEmpty()){
            response.put("msgCode", "notify001");
            response.put("message", "Tên người dùng không tồn tại");
            return response;
        }
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        List<NotificationModel> notificationModels = viewNotificationsService.getListNotification(request);
        for (NotificationModel ntf:
             notificationModels) {
            notificationDTOS.add(new NotificationDTO(ntf));
        }
        response.put("msgCode", "notify000");
        response.put("listNotification", notificationDTOS);
        return response;
    }

    @ResponseBody
    @PostMapping("/api-get-notification-number")
    public JSONObject getNotificationNumber(@RequestBody NotificationDTO request){
        JSONObject response = new JSONObject();
        if(request.getUsername() == null || request.getUsername().isEmpty()){
            response.put("msgCode", "notify001");
            response.put("message", "Tên người dùng không tồn tại");
            return response;
        }
        int notificationNumber = viewNotificationsService.getNotificationNumber(request);

        response.put("msgCode", notificationNumber != -1 ? "notify000" : "sys999");
        response.put("notificationNumber", notificationNumber);
        return response;
    }

    @ResponseBody
    @PostMapping("/api-change-notification-status")
    public JSONObject changeStatusNotification(@RequestBody NotificationDTO request){
        JSONObject response = new JSONObject();
        if(request.getUsername() == null || request.getUsername().isEmpty()){
            response.put("msgCode", "notify001");
            response.put("message", "Tên người dùng không tồn tại");
            return response;
        }
        NotificationModel notificationModel = viewNotificationsService.changeStatusNotification(request);

        response.put("msgCode", notificationModel != null ? "notify000" : "sys999");
        response.put("notification", new NotificationDTO(notificationModel));
        return response;
    }
}
