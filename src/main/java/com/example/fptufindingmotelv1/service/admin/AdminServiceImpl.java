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


    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
