package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.NotificationDTO;
import com.example.fptufindingmotelv1.model.NotificationModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManageNotificationServiceImpl implements ManageNotificationService{

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<NotificationModel> getListNotification(NotificationDTO notificationDTO) {
        try {
            List<NotificationModel> notificationModels =
                    notificationRepository.getAllByUsername(notificationDTO.getUsername());
            return notificationModels;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getNotificationNumber(NotificationDTO request) {
        try {
            return notificationRepository.getNotificationNumber(request.getUsername(), 12L);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public NotificationModel changeStatusNotification(NotificationDTO request) {
        try {
            NotificationModel notificationModel = notificationRepository.findById(request.getId()).get();
            StatusModel statusNotificationSeen = statusRepository.findByIdAndType(13, 4);
            notificationModel.setStatusNotification(statusNotificationSeen);
            return notificationRepository.save(notificationModel);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
