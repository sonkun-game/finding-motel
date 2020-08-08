package com.example.fptufindingmotelv1.service.renter;

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
public class RentalRequestServiceImpl implements RentalRequestService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    StatusRepository statusRepository;

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
                return responseMsg("111", message, null);
            }else if(message != null && message.equals("000")){
                RentalRequestModel rentalRequestModel = new RentalRequestModel();
                RoomModel roomModel = roomRepository.findById(rentalRequestDTO.getRoomId()).get();
                RenterModel renterModel = renterRepository.findByUsername(rentalRequestDTO.getRenterUsername());
                StatusModel statusModel = statusRepository.findById(rentalRequestDTO.getStatusId()).get();
                rentalRequestModel.setId(rentalRequestDTO.getId());
                rentalRequestModel.setRentalRoom(roomModel);
                rentalRequestModel.setRentalRenter(renterModel);
                rentalRequestModel.setRentalStatus(statusModel);

                Date date = new Date();
                Date createdDate = new Timestamp(date.getTime());
                SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT_ONLY_DATE);
                if(createdDate.after(rentalRequestDTO.getStartDate())){
                    return responseMsg("001", "Vui lòng chọn ngày bắt đầu sau ngày " + sdf.format(createdDate), null);
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
                return responseMsg("000", "Cập nhật thành công!", null);
            }
            return responseMsg("999", "Cập nhật không thành công!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    @Override
    public JSONObject changeStatus(String renterRqId) {
        try {
            RentalRequestModel rentalRequestModel = rentalRequestRepository.getOne(renterRqId);
            if(rentalRequestModel.getRentalStatus().getId() == 7){
                Date date = new Date();
                Date cancelDate = new Timestamp(date.getTime());
                StatusModel statusCancel = statusRepository.findByIdAndType(8, 3);
                rentalRequestModel.setRentalStatus(statusCancel);
                rentalRequestModel.setCancelDate(cancelDate);
                rentalRequestRepository.save(rentalRequestModel);
            }else if (rentalRequestModel.getRentalStatus().getId() == 9){
                StatusModel statusRoom = statusRepository.findByIdAndType(1, 1);
                rentalRequestModel.getRentalRoom().setStatus(statusRoom);
                roomRepository.save(rentalRequestModel.getRentalRoom());
                StatusModel statusExpire = statusRepository.findByIdAndType(11, 3);
                rentalRequestModel.setRentalStatus(statusExpire);
                rentalRequestModel = rentalRequestRepository.save(rentalRequestModel);
                // send notification to Landlord
                String notificationContent = "Tài khoản <b>" + rentalRequestModel.getRentalRenter().getUsername() +
                        "</b> đã kết thúc thuê phòng tại <b>" + rentalRequestModel.getRentalRoom().getName() +
                        "</b> - <b>" + rentalRequestModel.getRentalRoom().getPostRoom().getTitle() + "</b>";
                sendNotification(rentalRequestModel, notificationContent);

            }
            return responseMsg("000", "Cập nhật thành công!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("999", "Cập nhật không thành công!", null);
        }
    }

    @Override
    public JSONObject searchRentalRequest(RentalRequestDTO rentalRequestDTO) {
        try {
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, -30);
            Date cancelDateExpire = c.getTime();
            rentalRequestRepository.removeRentalRequest(cancelDateExpire, 8L, rentalRequestDTO.getRenterUsername());
            ArrayList<RentalRequestModel> renterModels = rentalRequestRepository.searchRentalRequest(
                    rentalRequestDTO.getId(), rentalRequestDTO.getRenterUsername(), rentalRequestDTO.getRoomId()
                    ,rentalRequestDTO.getStatusId(), rentalRequestDTO.getId());

            ArrayList<RentalRequestDTO> requestDTOS = new ArrayList<>();
            for (RentalRequestModel requestModel : renterModels) {
                RentalRequestDTO requestDTO = new RentalRequestDTO(requestModel);
                requestDTOS.add(requestDTO);
            }
            return renterModels.size() > 0 ? responseMsg("000", "OK", requestDTOS   )
                                            : responseMsg("111", "No Data!", null);
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("999", "System Error!", null);
        }
    }


    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content){
        try {
            // send notification to Landlord
            NotificationModel notificationModel = new NotificationModel();
            LandlordModel landlordModel = requestModel.getRentalRoom().getPostRoom().getLandlord();

            StatusModel statusNotification = statusRepository.findByIdAndType(12, 4);

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
