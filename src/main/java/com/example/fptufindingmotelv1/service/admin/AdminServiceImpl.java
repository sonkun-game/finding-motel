package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    PostModelRepository postModelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LandlordRepository landlordRepository;


    @Override
    public ArrayList<UserModel> getListUser() {
        return (ArrayList<UserModel>) userRepository.findAll();
    }

    @Override
    public UserModel getUserDetail(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ArrayList<UserModel> searchUser() {
        return null;
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

                landlord.setUnbanDate(calendar.getTime());
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
                landlord.setUnbanDate(null);
                landlordRepository.save(landlord);
                return (ArrayList<LandlordModel>) landlordRepository.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<PostModel> getListPost() {
        return null;
    }

    @Override
    public PostModel getPostDetail(String id) {
        return null;
    }

    @Override
    public ArrayList<PostModel> searchPost() {
        return null;
    }

    @Override
    public PostModel deletePost(String id) {
        return null;
    }

    @Override
    public ArrayList<ReportModel> getListReport() {
        return null;
    }
}
