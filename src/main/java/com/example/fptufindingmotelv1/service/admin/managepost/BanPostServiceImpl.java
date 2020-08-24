package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.NotificationRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class BanPostServiceImpl implements BanPostService{
    @Autowired
    PostRepository postRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RoomRepository roomRepository;
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public PostModel banPost(String postId) {
        PostModel postModel = postRepository.findById(postId).get();
        postModel.setBanned(true);

        // delete rental request of room of post
        StatusModel statusReject = new StatusModel(10L);
        StatusModel statusExpire = new StatusModel(11L);
        StatusModel statusRoomFree = new StatusModel(1L);
        String notificationContent;
        boolean isFreeRoom = true;

        for (RoomModel room :
                postModel.getRooms()) {

            if (room.getRoomRentals() != null && room.getRoomRentals().size() > 0) {
                for (RentalRequestModel request :
                        room.getRoomRentals()) {
                    if (request.getRentalStatus().getId() == 7) {
                        request.setRentalStatus(statusReject);
                        notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                                "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b> đã bị từ chối do bài đăng đã bị khóa";
                        // send notification to Renter
                        sendNotification(request, notificationContent);
                    } else if (request.getRentalStatus().getId() == 9) {
                        isFreeRoom = false;

                        request.setRentalStatus(statusExpire);
                        notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                                "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b> đã kết thúc do bài đăng đã bị khóa";
                        // send notification to Renter
                        sendNotification(request, notificationContent);
                    }
                }
                if (!isFreeRoom) {
                    room.setStatus(statusRoomFree);
                    roomRepository.save(room);
                }
            }
        }
        StatusModel statusReportPost = new StatusModel(4L);
        StatusModel statusReportAll = new StatusModel(6L);
        for (ReportModel report :
                postModel.getReports()) {
            if (report.getStatusReport().getId() == 3) {
                report.setStatusReport(statusReportPost);
            } else if (report.getStatusReport().getId() == 5) {
                report.setStatusReport(statusReportAll);
            }
        }
        postModel = postRepository.save(postModel);
        return postModel;
    }

    private NotificationModel sendNotification(RentalRequestModel requestModel, String content) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
