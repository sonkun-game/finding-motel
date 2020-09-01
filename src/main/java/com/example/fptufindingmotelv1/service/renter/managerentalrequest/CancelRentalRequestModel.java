package com.example.fptufindingmotelv1.service.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.RentalRequestRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class CancelRentalRequestModel implements CancelRentalRequestService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public JSONObject changeStatus(RentalRequestDTO rentalRequestDTO) {
        try {
            if(rentalRequestDTO.getStatusId() == 7){
                Date date = new Date();
                Date cancelDate = new Timestamp(date.getTime());
                rentalRequestRepository.updateCancelStatus(rentalRequestDTO.getId(), cancelDate, 8L);
            }else if (rentalRequestDTO.getStatusId() == 9){
                roomRepository.updateStatusRoom(rentalRequestDTO.getRoomId(), 1L);
                rentalRequestRepository.updateExpireStatus(rentalRequestDTO.getId(),
                        rentalRequestDTO.getExpireMessage(), 11L, null, null);
                // send notification to Landlord
                String notificationContent = "Tài khoản <b>" + rentalRequestDTO.getRenterUsername() +
                        "</b> đã kết thúc thuê phòng tại <b>" + rentalRequestDTO.getRoomName() +
                        "</b> - <b>" + rentalRequestDTO.getPostTitle() + "</b>";
                sendNotification(rentalRequestDTO, notificationContent);

            }
            return Constant.responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    private NotificationModel sendNotification(RentalRequestDTO rentalRequestDTO, String content){
        try {
            // send notification to Landlord
            NotificationModel notificationModel = new NotificationModel();
            UserModel userModel = new UserModel(rentalRequestDTO.getLandlordUsername());
            RentalRequestModel rentalRequestModel = new RentalRequestModel(rentalRequestDTO.getId());

            StatusModel statusNotification = new StatusModel(12L);

            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            notificationModel.setUserNotification(userModel);
            notificationModel.setContent(content);
            notificationModel.setStatusNotification(statusNotification);
            notificationModel.setCreatedDate(createdDate);
            notificationModel.setRentalRequestNotification(rentalRequestModel);
            return notificationRepository.save(notificationModel);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
