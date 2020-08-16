package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    RentalRequestRepository rentalRequestRepository;

    @Autowired
    PaymentPackageRepository paymentPackageRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    RoomRepository roomRepository;

    protected JSONObject getResponeMessage(String code, String msg) {
        JSONObject responeMsg = new JSONObject();
        responeMsg.put("code", code);
        responeMsg.put("message", msg);
        return responeMsg;
    }

    @Override
    public JSONObject searchUsers(UserDTO userDTO, Pageable pageable) {
        try {
            List<UserDTO> users = new ArrayList<>();
            Page<UserModel> userModels = userRepository.searchUser(userDTO.getUsername(), userDTO.getRoleId(), pageable);
            for (UserModel u : userModels.getContent()) {
                UserDTO user = new UserDTO(u);
                //add report number and ban available
                if (u.getRole().getId() == 2) {
                    boolean banAvailable = user.getUnBanDate() == null
                            && user.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_USER;
                    user.setBanAvailable(banAvailable);
                }
                users.add(user);
            }
            JSONObject pagination = paginationModel(userModels);
            JSONObject resposnse = responseMsg("000", "Success", users);
            resposnse.put("pagination", pagination);
            return resposnse;
        }catch (Exception e){
            e.printStackTrace();
            return responseMsg("777", "Lỗi dữ liệu!", null);
        }
    }

    @Override
    public LandlordModel banLandlord(String username) {
        try {
            LandlordModel landlord = landlordRepository.findByUsername(username);
            if (landlord == null) {
                return null;
            } else {
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));

                String currentDate = dateFormat.format(new Date());
                Date date = dateFormat.parse(currentDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_MONTH, 14);

                landlord.setUnBanDate(calendar.getTime());
                for (PostModel post:
                     landlord.getPosts()) {
                    post.setVisible(false);
                    for (ReportModel report:
                         post.getReports()) {
                        if(report.getStatusReport().getId() == 3){
                            StatusModel statusModel = new StatusModel(5L);
                            report.setStatusReport(statusModel);
                        }else if(report.getStatusReport().getId() == 4){
                            StatusModel statusModel = new StatusModel(6L);
                            report.setStatusReport(statusModel);
                        }
                    }
                }
                return landlordRepository.save(landlord);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public LandlordModel unbanLandlord(String username) {
        try {
            LandlordModel landlord = landlordRepository.findByUsername(username);
            if (landlord == null) {
                return null;
            } else {
                landlord.setUnBanDate(null);
                for (PostModel post:
                        landlord.getPosts()) {
                    post.setVisible(true);
                }
                return landlordRepository.save(landlord);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<PostResponseDTO> getListPost() {
        try {
            ArrayList<PostResponseDTO> posts = new ArrayList<>();
            for (PostModel post : postRepository.findAll()) {
                PostResponseDTO postResponseDTO = new PostResponseDTO(post);
                boolean banAvailable = postResponseDTO.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_POST;
                postResponseDTO.setBanAvailable(banAvailable);
                posts.add(postResponseDTO);
            }
            return posts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteReport(String id) {
        reportRepository.deleteById(id);
    }

    @Override
    public ArrayList<PostResponseDTO> searchPost(PostSearchDTO postSearchDTO) {
        try {
            ArrayList<PostModel> posts = (ArrayList<PostModel>) postRepository.searchPost(postSearchDTO.getLandlordUsername(),
                    postSearchDTO.getTitle(), postSearchDTO.getPriceMax(), postSearchDTO.getPriceMin(),
                    postSearchDTO.getDistanceMax(), postSearchDTO.getDistanceMin(),
                    postSearchDTO.getSquareMax(), postSearchDTO.getSquareMin(), postSearchDTO.getVisible(), postSearchDTO.getTypeId(), null);
            ArrayList<PostResponseDTO> postResponseDTOs = new ArrayList<>();
            for (PostModel p : posts) {
                PostResponseDTO pr = new PostResponseDTO(p);
                boolean banAvailable = pr.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_POST;
                pr.setBanAvailable(banAvailable);
                postResponseDTOs.add(pr);
            }
            return postResponseDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

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

        for (RoomModel room:
                postModel.getRooms()) {

            if(room.getRoomRentals() != null && room.getRoomRentals().size() > 0){
                for (RentalRequestModel request:
                     room.getRoomRentals()) {
                    if(request.getRentalStatus().getId() == 7){
                        request.setRentalStatus(statusReject);
                        notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                                "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b> đã bị từ chối do bài đăng đã bị khóa";
                        // send notification to Renter
                        sendNotification(request, notificationContent);
                    }else if(request.getRentalStatus().getId() == 9){
                        isFreeRoom = false;

                        request.setRentalStatus(statusExpire);
                        notificationContent = "Yêu cầu thuê trọ vào <b>" + request.getRentalRoom().getName() +
                                "</b> - <b>" + request.getRentalRoom().getPostRoom().getTitle() + "</b> đã kết thúc do bài đăng đã bị khóa";
                        // send notification to Renter
                        sendNotification(request, notificationContent);
                    }
                }
                if(!isFreeRoom){
                    room.setStatus(statusRoomFree);
                    roomRepository.save(room);
                }
            }
        }
        StatusModel statusReportPost = new StatusModel(4L);
        StatusModel statusReportAll = new StatusModel(6L);
        for (ReportModel report:
                postModel.getReports()) {
            if(report.getStatusReport().getId() == 3){
                report.setStatusReport(statusReportPost);
            }else if(report.getStatusReport().getId() == 5){
                report.setStatusReport(statusReportAll);
            }
        }
        postModel = postRepository.save(postModel);
        return postModel;
    }

    @Override
    public JSONObject searchReport(ReportRequestDTO reportRequestDTO, Pageable pageable) {
        try {
            Page<ReportModel> reportModels = reportRepository.searchReport(reportRequestDTO.getLandlordId(),
                    reportRequestDTO.getRenterId(), reportRequestDTO.getPostTitle(),
                    reportRequestDTO.getStatusReport(), pageable);
            ArrayList<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
            for (ReportModel report : reportModels.getContent()) {
                ReportResponseDTO responseDTO = new ReportResponseDTO(report);
                reportResponseDTOS.add(responseDTO);
            }
            JSONObject response = responseMsg("000", "Success", reportResponseDTOS);
            response.put("pagination", paginationModel(reportModels));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("777", "Lỗi dữ liệu.", null);
        }
    }

    @Override
    public JSONObject getInitAdminManager() {
        JSONObject response = new JSONObject();
        try {
            // get list status report
            List<StatusModel> listStatus = statusRepository.findAllByType(2);
            List<StatusDTO> listStatusReport = new ArrayList<>();
            for (StatusModel status:
                    listStatus) {
                listStatusReport.add(new StatusDTO(status));
            }
            response.put("code", "000");
            response.put("listStatusReport", listStatusReport);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống!");
            return response;
        }
    }

    @Override
    public PaymentPackageModel savePaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel = new PaymentPackageModel();
            if(paymentPackageDTO.getId() != null){
                 paymentPackageModel =
                        paymentPackageRepository.findById(paymentPackageDTO.getId()).get();
            }
            paymentPackageModel.setPackageName(paymentPackageDTO.getPackageName());
            paymentPackageModel.setDuration(paymentPackageDTO.getDuration());
            paymentPackageModel.setAmount(paymentPackageDTO.getAmount());
            paymentPackageModel.setAvailable(true);
            return paymentPackageRepository.save(paymentPackageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PaymentPackageModel changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel =
                    paymentPackageRepository.findById(paymentPackageDTO.getId()).get();
            paymentPackageModel.setAvailable(!paymentPackageModel.isAvailable());
            return paymentPackageRepository.save(paymentPackageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public LandlordModel addMoneyForLandlord(PaymentDTO paymentDTO) {
        PaymentModel paymentModel = new PaymentModel();
        LandlordModel landlordModel = landlordRepository.findByUsername(paymentDTO.getLandlord());
        landlordModel.setAmount(landlordModel.getAmount() + paymentDTO.getAmount());
        landlordModel = landlordRepository.save(landlordModel);

        Date date = new Date();
        Date payDate = new Timestamp(date.getTime());
        paymentModel.setLandlordModel(landlordModel);
        paymentModel.setAmount(paymentDTO.getAmount());
        paymentModel.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentModel.setNote(paymentDTO.getNote());
        paymentModel.setPayDate(payDate);
        paymentRepository.save(paymentModel);
        return landlordModel;
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

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    public JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }
}
