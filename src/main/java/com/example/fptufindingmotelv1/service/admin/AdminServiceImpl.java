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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        LandlordModel landlord = landlordRepository.findByUsername(username);
        Date date = landlord.getUnbanDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        date = calendar.getTime();
        System.err.println(date);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
//        String dateWithSeconds = simpleDateFormat.format(date);

        return (ArrayList<LandlordModel>) landlordRepository.findAll();
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
