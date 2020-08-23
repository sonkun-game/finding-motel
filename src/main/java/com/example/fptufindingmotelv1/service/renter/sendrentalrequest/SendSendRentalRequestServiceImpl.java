package com.example.fptufindingmotelv1.service.renter.sendrentalrequest;

import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SendSendRentalRequestServiceImpl implements SendRentalRequestService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    public String checkExitRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            RenterModel renterModel = (RenterModel) userRepository.findByUsername(rentalRequestDTO.getRenterUsername());
            RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
            List<RentalRequestModel> rentalRequestModels = rentalRequestRepository
                    .findByRentalRenterAndRentalRoom(renterModel, roomModel);
            for (RentalRequestModel request:
                 rentalRequestModels) {
                if(request != null &&
                        request.getRentalStatus().getId() == 9){
                    return "Bạn đã thuê phòng này!";
                }
                else if(request != null &&
                        request.getRentalStatus().getId() == 7){
                    return "Bạn đã yêu cầu thuê phòng này!";
                }
            }
            List<RentalRequestModel> requestModels =
                    rentalRequestRepository.getListRequest(null, 9L, renterModel.getUsername(), null);
            if(requestModels != null && requestModels.size() > 0){
                return "Bạn không thể thực hiện yêu cầu thuê phòng vì đã thuê một phòng khác!";
            }
            return "000";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject sentRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            String message = checkExitRentalRequest(rentalRequestDTO);
            if (message != null && !message.equals("000")) {
                return Constant.responseMsg("111", message, null);
            }else if(message != null && message.equals("000")){
                RentalRequestModel rentalRequestModel = new RentalRequestModel();
                RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
                RenterModel renterModel = new RenterModel(rentalRequestDTO.getRenterUsername());
                StatusModel statusModel = new StatusModel(rentalRequestDTO.getStatusId());
                rentalRequestModel.setId(rentalRequestDTO.getId());
                rentalRequestModel.setRentalRoom(roomModel);
                rentalRequestModel.setRentalRenter(renterModel);
                rentalRequestModel.setRentalStatus(statusModel);

                Date date = new Date();
                Date createdDate = new Timestamp(date.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_ONLY_DATE);
                if(createdDate.after(rentalRequestDTO.getStartDate())){
                    return Constant.responseMsg("001", "Vui lòng chọn ngày bắt đầu sau ngày " + sdf.format(createdDate), null);
                }
                //set start date
                rentalRequestModel.setStartDate(rentalRequestDTO.getStartDate());
                rentalRequestModel.setRequestDate(createdDate);

                rentalRequestModel = rentalRequestRepository.save(rentalRequestModel);
                // send notification to Landlord
                String notificationContent = "Tài khoản <b>" + rentalRequestModel.getRentalRenter().getUsername() +
                        "</b> đã gửi một yêu cầu thuê trọ vào <b>" + rentalRequestModel.getRentalRoom().getName() +
                        "</b> - <b>" + rentalRequestModel.getRentalRoom().getPostRoom().getTitle() + "</b>";
                sendNotification(rentalRequestModel, notificationContent);
                return Constant.responseMsg("000", "Cập nhật thành công!", null);
            }
            return Constant.responseMsg("999", "Cập nhật không thành công!", null);
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
