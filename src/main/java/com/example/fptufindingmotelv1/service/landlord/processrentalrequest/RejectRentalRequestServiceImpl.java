package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.NotificationModel;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RejectRentalRequestServiceImpl implements RejectRentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public RentalRequestModel rejectRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            StatusModel statusReject = new StatusModel(10L);
            rentalRequestModel.setRentalStatus(statusReject);
            String notificationContent = "Chủ trọ <b>" + rentalRequestModel.getRentalRoom().getPostRoom().getLandlord().getUsername() +
                    "</b> đã từ chối yêu cầu thuê trọ vào <b>" + rentalRequestModel.getRentalRoom().getName() +
                    "</b> - <b>" + rentalRequestModel.getRentalRoom().getPostRoom().getTitle() + "</b>";
            // send notification to Renter
            sendNotification(rentalRequestModel, notificationContent);
            return rentalRequestRepository.save(rentalRequestModel);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private NotificationModel sendNotification(RentalRequestModel requestModel, String content){
        try {
            // send notification to Renter
            NotificationModel notificationModel = new NotificationModel();
            RenterModel renterModel = requestModel.getRentalRenter();

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
