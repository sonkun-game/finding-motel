package com.example.fptufindingmotelv1.service.landlord.manageroom;

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
public class ChangeRoomStatusServiceImpl implements ChangeRoomStatusService {

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public RoomModel changeRoomStatus(RentalRequestDTO rentalRequestDTO) {
        try {
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            if(roomModel.getStatus().getId() == 1){

                StatusModel statusRoom = new StatusModel(2L);
                roomModel.setStatus(statusRoom);
                roomModel = roomRepository.save(roomModel);
                if(roomModel.getRoomRentals() != null && roomModel.getRoomRentals().size() > 0){
                    StatusModel statusReject = new StatusModel(10L);
                    for (RentalRequestModel request:
                         roomModel.getRoomRentals()) {
                        request.setRentalStatus(statusReject);
                        String notificationContent = "Chủ trọ <b>" + roomModel.getPostRoom().getLandlord().getUsername() +
                                "</b> đã từ chối yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                                "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b>";
                        // send notification to Renter
                        sendNotification(request, notificationContent);
                    }
                    rentalRequestRepository.saveAll(roomModel.getRoomRentals());
                }
            }else if(roomModel.getStatus().getId() == 2){
                StatusModel statusRoom = new StatusModel(1L);
                roomModel.setStatus(statusRoom);
                roomModel = roomRepository.save(roomModel);
                List<RentalRequestModel> requestModels = rentalRequestRepository.getListRequest(
                        null, 9L, null, roomModel.getId());
                if(requestModels != null && requestModels.size() > 0){
                    StatusModel statusExpire = new StatusModel(11L);
                    requestModels.get(0).setRentalStatus(statusExpire);
                    requestModels.get(0).setExpireMessage(rentalRequestDTO.getExpireMessage());
                    rentalRequestRepository.save(requestModels.get(0));
                    String notificationContent = "Chủ trọ <b>" + roomModel.getPostRoom().getLandlord().getUsername() +
                            "</b> đã kết thúc cho thuê phòng tại <b>" + requestModels.get(0).getRentalRoom().getName() +
                            "</b> - <b>" + requestModels.get(0).getRentalRoom().getPostRoom().getTitle() + "</b>";
                    // send notification to Renter
                    sendNotification(requestModels.get(0), notificationContent);
                }
            }
            return roomModel;
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
