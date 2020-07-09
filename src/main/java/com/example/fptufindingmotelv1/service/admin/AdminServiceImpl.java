package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
                    userDTO.setReportNumber(getReportNumber(user.getUsername()));
                    boolean banAvailable = userDTO.getUnBanDate() == null && userDTO.getReportNumber() >= 3;
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

    public Integer getReportNumber(String username) {
        Integer reportNumber = 0;
        ArrayList<ReportResponseDTO> listReport = getListReport();
        for (ReportResponseDTO report : listReport) {
            if (report.getLandlordName().equals(username)) {
                reportNumber++;
            }
        }
        return reportNumber;
    }

    @Override
    public ArrayList<UserDTO> searchUserByUsernameOrDisplayName(String username) {
        ArrayList<UserDTO> users = new ArrayList<>();
        for (UserModel user : userRepository.findByUsernameOrDisplayName(username)) {
            UserDTO userDTO = new UserDTO(user);
            //add report number and ban available
            if (user.getRole().getId() == 2) {
                userDTO.setReportNumber(getReportNumber(user.getUsername()));
                boolean banAvailable = userDTO.getUnBanDate() == null && userDTO.getReportNumber() >= 3;
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
                postResponseDTOs.add(pr);
            }
            return postResponseDTOs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void banPost(String postId) {
        PostModel postModel = postRepository.getOne(postId);
    }
}
