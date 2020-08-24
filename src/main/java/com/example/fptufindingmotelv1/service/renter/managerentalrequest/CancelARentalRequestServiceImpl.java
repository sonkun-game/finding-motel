package com.example.fptufindingmotelv1.service.renter.managerentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.NotificationModel;
import com.example.fptufindingmotelv1.model.RentalRequestModel;
import com.example.fptufindingmotelv1.model.StatusModel;
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
public class CancelARentalRequestServiceImpl implements CancelARentalRequestService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public JSONObject changeStatus(RentalRequestDTO rentalRequestDTO) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.findById(rentalRequestDTO.getId()).get();
            if(rentalRequestModel.getRentalStatus().getId() == 7){
                Date date = new Date();
                Date cancelDate = new Timestamp(date.getTime());
                StatusModel statusCancel = new StatusModel(8L);
                rentalRequestModel.setRentalStatus(statusCancel);
                rentalRequestModel.setCancelDate(cancelDate);
                rentalRequestRepository.save(rentalRequestModel);
            }else if (rentalRequestModel.getRentalStatus().getId() == 9){
                StatusModel statusRoom = new StatusModel(1L);
                rentalRequestModel.getRentalRoom().setStatus(statusRoom);
                roomRepository.save(rentalRequestModel.getRentalRoom());
                StatusModel statusExpire = new StatusModel(11L);
                rentalRequestModel.setRentalStatus(statusExpire);
                rentalRequestModel.setExpireMessage(rentalRequestDTO.getExpireMessage());
                rentalRequestModel = rentalRequestRepository.save(rentalRequestModel);
                // send notification to Landlord
                String notificationContent = "Tài khoản <b>" + rentalRequestModel.getRentalRenter().getUsername() +
                        "</b> đã kết thúc thuê phòng tại <b>" + rentalRequestModel.getRentalRoom().getName() +
                        "</b> - <b>" + rentalRequestModel.getRentalRoom().getPostRoom().getTitle() + "</b>";
                sendNotification(rentalRequestModel, notificationContent);

            }
            return Constant.responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content){
        try {
            // send notification to Landlord
            NotificationModel notificationModel = new NotificationModel();
            LandlordModel landlordModel = requestModel.getRentalRoom().getPostRoom().getLandlord();

            StatusModel statusNotification = new StatusModel(12L);

            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            notificationModel.setUserNotification(landlordModel);
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
