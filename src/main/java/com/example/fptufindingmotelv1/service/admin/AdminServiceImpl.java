package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

    protected JSONObject getResponeMessage(String code, String msg) {
        JSONObject responeMsg = new JSONObject();
        responeMsg.put("code", code);
        responeMsg.put("message", msg);
        return responeMsg;
    }

    @Override
    public List<UserDTO> searchUsers(UserDTO userDTO) {
        try {
            List<UserDTO> users = new ArrayList<>();
            List<UserModel>  userModels = userRepository.searchUser(userDTO.getUsername(), userDTO.getRoleId());
            for (UserModel u : userModels) {
                UserDTO user = new UserDTO(u);
                //add report number and ban available
                if (u.getRole().getId() == 2) {
                    boolean banAvailable = user.getUnBanDate() == null
                            && user.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_USER;
                    user.setBanAvailable(banAvailable);
                }
                users.add(user);
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<LandlordModel> banLandlord(String username) {
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
                            StatusModel statusModel = statusRepository.findByIdAndType(5, 2);
                            report.setStatusReport(statusModel);
                        }else if(report.getStatusReport().getId() == 4){
                            StatusModel statusModel = statusRepository.findByIdAndType(6, 2);
                            report.setStatusReport(statusModel);
                        }
                    }
                }
                landlordRepository.save(landlord);
                return (ArrayList<LandlordModel>) landlordRepository.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public ArrayList<LandlordModel> unbanLandlord(String username) {
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
                landlordRepository.save(landlord);
                return (ArrayList<LandlordModel>) landlordRepository.findAll();
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
    public void deletePost(String id) {
        postRepository.deleteById(id);
    }

    @Override
    public ArrayList<ReportResponseDTO> getListReport() {
        try {
            ArrayList<ReportResponseDTO> requestDTOs = new ArrayList<>();
            for (ReportModel report : reportRepository.findAll()) {
                ReportResponseDTO reportRequest = new ReportResponseDTO(report);
                requestDTOs.add(reportRequest);
            }
            return requestDTOs;
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

    @Override
    public PostModel banPost(String postId) {
        try {
            PostModel postModel = postRepository.findById(postId).get();
            postModel.setBanned(true);
            // delete rental request of room of post
            for (RoomModel room:
                    postModel.getRooms()) {
                if(room.getRoomRentals() != null && room.getRoomRentals().size() > 0){
                    rentalRequestRepository.deleteAll(room.getRoomRentals());
                }
            }
            for (ReportModel report:
                    postModel.getReports()) {
                if(report.getStatusReport().getId() == 3){
                    StatusModel statusModel = statusRepository.findByIdAndType(4, 2);
                    report.setStatusReport(statusModel);
                }else if(report.getStatusReport().getId() == 5){
                    StatusModel statusModel = statusRepository.findByIdAndType(6, 2);
                    report.setStatusReport(statusModel);
                }
            }
            postModel = postRepository.save(postModel);
            return postModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PostModel unBanPost(String postId) {
        try {
            PostModel postModel = postRepository.findById(postId).get();
            postModel.setBanned(false);
            postModel = postRepository.save(postModel);
            return postModel;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ReportResponseDTO> searchReport(ReportRequestDTO reportRequestDTO) {
        try {
            List<ReportModel> reportModels = reportRepository.searchReport(reportRequestDTO.getLandlordId(),
                    reportRequestDTO.getRenterId(), reportRequestDTO.getPostTitle(),
                    reportRequestDTO.getStatusReport());
            ArrayList<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
            for (ReportModel report : reportModels) {
                ReportResponseDTO responseDTO = new ReportResponseDTO(report);
                reportResponseDTOS.add(responseDTO);
            }
            return reportResponseDTOS;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            response.put("msgCode", "admin000");
            response.put("listStatusReport", listStatusReport);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msgCode", "sys999");
            response.put("message", e.getMessage());
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
}
