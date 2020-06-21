package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;

import java.util.ArrayList;

public interface AdminService {
    public ArrayList<UserModel> getListUser();

    public UserModel getUserDetail(String username);

    public ArrayList<UserModel> searchUser();

    public ArrayList<LandlordModel> banLandlord(String username);
    public ArrayList<LandlordModel> unbanLandlord(String username);

    public ArrayList<PostResponseDTO> getListPost();

    public PostResponseDTO getPostDetail(Long id);

    public ArrayList<PostModel> searchPost();

    public void deletePost(Long id);

    public ArrayList<ReportResponseDTO> getListReport();

    public void deleteReport(Long id);

}
