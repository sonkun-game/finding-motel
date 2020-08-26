package com.example.fptufindingmotelv1.service.landlord.processrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class AcceptRentalRequestModel implements AcceptRentalRequestService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<RentalRequestModel> acceptRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            StatusModel statusAccept = new StatusModel(9L);
            StatusModel statusReject = new StatusModel(10L);
            for (RentalRequestModel request:
                 roomModel.getRoomRentals()) {
                if(!request.getId().equals(rentalRequestModel.getId())
                        && request.getRentalStatus().getId() == 7){
                    request.setRentalStatus(statusReject);
                    String notificationContent = "Chủ trọ <b>" + roomModel.getPostRoom().getLandlord().getUsername() +
                            "</b> đã từ chối yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                            "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b>";
                    // send notification to Renter
                    sendNotification(request, notificationContent);
                }else if(request.getId().equals(rentalRequestModel.getId()) ){
                    request.setRentalStatus(statusAccept);
                    String notificationContent = "Chủ trọ <b>" + roomModel.getPostRoom().getLandlord().getUsername() +
                            "</b> đã chấp nhận yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                            "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b>";
                    // send notification to Renter
                    sendNotification(request, notificationContent);
                }
            }
            Date date = new Date();
            Date cancelDate = new Timestamp(date.getTime());
            StatusModel statusCancel = new StatusModel(8L);
            RenterModel renter = rentalRequestModel.getRentalRenter();
            for (RentalRequestModel request:
                 renter.getRenterRentals()) {
                if(!request.getId().equals(rentalRequestModel.getId())
                        && request.getRentalStatus().getId() == 7){
                    request.setRentalStatus(statusCancel);
                    request.setCancelDate(cancelDate);
                }
            }
            StatusModel statusRoom = new StatusModel(2L);
            roomModel.setStatus(statusRoom);
            rentalRequestRepository.saveAll(renter.getRenterRentals());
            roomRepository.save(roomModel);
            List<RentalRequestModel> response = rentalRequestRepository.saveAll(roomModel.getRoomRentals());
            return response;
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
