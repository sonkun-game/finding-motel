package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    protected JSONObject getResponeMessage(String code, String msg) {
        JSONObject responeMsg = new JSONObject();
        responeMsg.put("code", code);
        responeMsg.put("message", msg);
        return responeMsg;
    }

    @Override
    public ArrayList<UserDTO> getListUser() {
        try {
            ArrayList<UserDTO> users = new ArrayList<>();
            for (UserModel user : userRepository.findAll()) {
                UserDTO userDTO = new UserDTO(user);
                //add report number
                if (user.getRole().getId() == 2) {
                    boolean banAvailable = userDTO.getUnBanDate() == null
                            && userDTO.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_USER;
                    userDTO.setBanAvailable(banAvailable);
                }
                //add user to list
                users.add(userDTO);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public ArrayList<UserDTO> searchUserByUsernameOrDisplayName(String username) {
        ArrayList<UserDTO> users = new ArrayList<>();
        for (UserModel user : userRepository.findByUsernameOrDisplayName(username)) {
            UserDTO userDTO = new UserDTO(user);
            //add report number and ban available
            if (user.getRole().getId() == 2) {
                boolean banAvailable = userDTO.getUnBanDate() == null
                        && userDTO.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_USER;
                userDTO.setBanAvailable(banAvailable);
            }
            users.add(userDTO);
        }
        return users;
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
                    post.setBanned(true);
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
                    post.setBanned(false);
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
                boolean banAvailable = post.getLandlord().getUnBanDate() == null
                        && postResponseDTO.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_POST;
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
                    postSearchDTO.getSquareMax(), postSearchDTO.getSquareMin(), postSearchDTO.getVisible(), postSearchDTO.getTypeId());
            ArrayList<PostResponseDTO> postResponseDTOs = new ArrayList<>();
            for (PostModel p : posts) {
                PostResponseDTO pr = new PostResponseDTO(p);
                boolean banAvailable = p.getLandlord().getUnBanDate() == null
                        && pr.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_POST;
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
}
