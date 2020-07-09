package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.*;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.UserModel;

import java.util.ArrayList;

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

    Boolean banPost(String postId) ;

}
