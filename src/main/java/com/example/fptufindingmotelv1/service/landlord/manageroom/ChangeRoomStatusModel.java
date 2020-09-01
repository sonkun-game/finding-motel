package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ChangeRoomStatusModel implements ChangeRoomStatusService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean changeRoomStatus(RentalRequestDTO rentalRequestDTO) {
        if(rentalRequestDTO.getStatusId() == 1){
            List<RentalRequestModel> listRequestOfRoom =
                    rentalRequestRepository.getListRequestIdByRoom(rentalRequestDTO.getRoomId(), 7L);
            roomRepository.updateStatusRoom(rentalRequestDTO.getRoomId(), 2L);
            if(listRequestOfRoom != null && listRequestOfRoom.size() > 0){
                StatusModel statusReject = new StatusModel(10L);
                for (RentalRequestModel request:
                        listRequestOfRoom) {
                    request.setRentalStatus(statusReject);
                    String notificationContent = "Chủ trọ <b>" + rentalRequestDTO.getLandlordUsername() +
                            "</b> đã từ chối yêu cầu thuê trọ vào <b>" + rentalRequestDTO.getRoomName() +
                            "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
                    // send notification to Renter
                    sendNotification(request, notificationContent);
                }
                rentalRequestRepository.updateStatus(null, 10L,
                        rentalRequestDTO.getRoomId(), 7L);
            }
        }else if(rentalRequestDTO.getStatusId() == 2){
            roomRepository.updateStatusRoom(rentalRequestDTO.getRoomId(), 1L);

            List<RentalRequestModel> requestAccepted = rentalRequestRepository.getListRequestIdByRoom(
                    rentalRequestDTO.getRoomId(), 9L);
            if(requestAccepted != null && requestAccepted.size() > 0){
                rentalRequestRepository.updateExpireStatus(null, rentalRequestDTO.getExpireMessage(),
                        11L, rentalRequestDTO.getRoomId(), 9L);
                String notificationContent = "Chủ trọ <b>" + rentalRequestDTO.getLandlordUsername() +
                        "</b> đã kết thúc cho thuê phòng tại <b>" + rentalRequestDTO.getRoomName() +
                        "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
                // send notification to Renter
                sendNotification(requestAccepted.get(0), notificationContent);
            }
        }
        return true;
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content){
        try {
            // send notification to Renter
            NotificationModel notificationModel = new NotificationModel();
            UserModel renterModel = new UserModel(requestModel.getRentalRenter().getUsername());

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
