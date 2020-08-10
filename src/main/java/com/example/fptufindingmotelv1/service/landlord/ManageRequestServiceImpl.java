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
    private LandlordRepository landlordRepository;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<RoomModel> getListRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            if(rentalRequestDTO.getLandlordUsername() != null && !rentalRequestDTO.getLandlordUsername().isEmpty()){
                LandlordModel landlordModel = landlordRepository.findByUsername(rentalRequestDTO.getLandlordUsername());
                return roomRepository.getListRoom(landlordModel.getUsername(),
                        rentalRequestDTO.getPostId(), rentalRequestDTO.getRoomId());
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RentalRequestModel> acceptRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            StatusModel statusAccept = statusRepository.findByIdAndType(9, 3);
            StatusModel statusReject = statusRepository.findByIdAndType(10, 3);
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
            StatusModel statusCancel = statusRepository.findByIdAndType(8, 3);
            RenterModel renter = rentalRequestModel.getRentalRenter();
            for (RentalRequestModel request:
                 renter.getRenterRentals()) {
                if(!request.getId().equals(rentalRequestModel.getId())
                        && request.getRentalStatus().getId() == 7){
                    request.setRentalStatus(statusCancel);
                    request.setCancelDate(cancelDate);
                }
            }
            StatusModel statusRoom = statusRepository.findByIdAndType(2, 1);
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
            StatusModel statusReject = statusRepository.findByIdAndType(10, 3);
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
    public RoomModel changeRoomStatus(RoomDTO roomDTO) {
        try {
            RoomModel roomModel = roomRepository.findById(roomDTO.getRoomId()).get();
            if(roomModel.getStatus().getId() == 1){

                StatusModel statusRoom = statusRepository.findByIdAndType(2, 1);
                roomModel.setStatus(statusRoom);
                roomModel = roomRepository.save(roomModel);
                if(roomModel.getRoomRentals() != null && roomModel.getRoomRentals().size() > 0){
                    StatusModel statusReject = statusRepository.findByIdAndType(10, 3);
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
                StatusModel statusRoom = statusRepository.findByIdAndType(1, 1);
                roomModel.setStatus(statusRoom);
                roomModel = roomRepository.save(roomModel);
                List<RentalRequestModel> requestModels = rentalRequestRepository.getListRequest(
                        null, 9L, null, roomModel.getId());
                if(requestModels != null && requestModels.size() > 0){
                    StatusModel statusExpire = statusRepository.findByIdAndType(11, 3);
                    requestModels.get(0).setRentalStatus(statusExpire);
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

            StatusModel statusNotification = statusRepository.findByIdAndType(12, 4);

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
