package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ManageRequestServiceImpl implements ManageRequestService{

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private RenterRepository renterRepository;

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

    @Override
    public List<RentalRequestModel> getListRequestByRoom(RentalRequestDTO rentalRequestDTO) {
        try {
            return rentalRequestRepository.getRentalRequests(rentalRequestDTO.getId(), rentalRequestDTO.getRoomId(), rentalRequestDTO.getStatusId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RenterModel getRenter(RentalRequestDTO rentalRequestDTO) {
        try {
            return renterRepository.getRenterByUsername(rentalRequestDTO.getRenterUsername());
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
