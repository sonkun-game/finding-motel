package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public interface AdminService {
     ArrayList<UserDTO> getListUser();

     ArrayList<UserDTO> searchUserByUsernameOrDisplayName(String username);

     ArrayList<LandlordModel> banLandlord(String username);
     ArrayList<LandlordModel> unbanLandlord(String username);

     ArrayList<PostResponseDTO> getListPost();

     void deletePost(String id);

     ArrayList<ReportResponseDTO> getListReport();

     void deleteReport(String id);

    ArrayList<PostResponseDTO> searchPost(PostSearchDTO postSearchDTO);

    PostModel banPost(String postId);
    PostModel unBanPost(String postId);

    List<ReportResponseDTO> searchReport(ReportRequestDTO reportRequestDTO);

    JSONObject getInitAdminManager();

}
