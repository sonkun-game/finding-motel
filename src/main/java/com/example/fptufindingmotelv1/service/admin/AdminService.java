package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.ReportModel;
import com.example.fptufindingmotelv1.model.UserModel;

import java.util.ArrayList;

public interface AdminService {
    public ArrayList<UserModel> getListUser();

    public UserModel getUserDetail(String username);

    public ArrayList<UserModel> searchUser();

    public ArrayList<LandlordModel> banLandlord(String username);
    public ArrayList<LandlordModel> unbanLandlord(String username);

    public ArrayList<PostModel> getListPost();

    public PostModel getPostDetail(Long id);

    public ArrayList<PostModel> searchPost();

    public PostModel deletePost(Long id);

    public ArrayList<ReportModel> getListReport();

}
