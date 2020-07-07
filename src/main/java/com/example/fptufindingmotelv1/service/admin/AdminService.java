package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.ReportResponseDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;

import java.util.ArrayList;

public interface AdminService {
    public ArrayList<UserDTO> getListUser();

    public UserModel getUserDetail(String username);

    public ArrayList<UserDTO> searchUserByUsernameOrDisplayName(String username);

    public ArrayList<LandlordModel> banLandlord(String username);
    public ArrayList<LandlordModel> unbanLandlord(String username);

    public ArrayList<PostResponseDTO> getListPost();

    public PostResponseDTO getPostDetail(String id);

    public void deletePost(String id);
    public void deletePost(Long id);

    public ArrayList<ReportResponseDTO> getListReport();

    public void deleteReport(Long id);

    ArrayList<PostResponseDTO> searchPost();

}
