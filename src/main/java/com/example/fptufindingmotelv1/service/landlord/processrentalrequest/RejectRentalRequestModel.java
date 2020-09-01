package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RejectRentalRequestModel implements RejectRentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean rejectRentalRequest(RentalRequestDTO rentalRequestDTO) {
        rentalRequestRepository.updateStatus(rentalRequestDTO.getId(), 10L, null, null);
        String notificationContent = "Chủ trọ <b>" + rentalRequestDTO.getLandlordUsername() +
                "</b> đã từ chối yêu cầu thuê trọ vào <b>" + rentalRequestDTO.getRoomName() +
                "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
        // send notification to Renter
        sendNotification(rentalRequestDTO, notificationContent);
        return true;
    }


    private NotificationModel sendNotification(RentalRequestDTO rentalRequestDTO, String content){
        try {
            // send notification to Renter
            NotificationModel notificationModel = new NotificationModel();
            UserModel renterModel = new UserModel(rentalRequestDTO.getRenterUsername());
            RentalRequestModel requestModel = new RentalRequestModel(rentalRequestDTO.getId());

            StatusModel statusNotification = new StatusModel(12L);
            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            notificationModel.setUserNotification(renterModel);
            notificationModel.setContent(content);
            notificationModel.setStatusNotification(statusNotification);
            notificationModel.setCreatedDate(createdDate);
            notificationModel.setRentalRequestNotification(requestModel);
            return notificationRepository.save(notificationModel);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
