package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchUserModel implements SearchUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    public JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }

    @Override
    public JSONObject searchUsers(UserDTO userDTO, Pageable pageable) {
        try {
            Long reportNumber = 0L;
            Page<UserModel> userModels = userRepository.searchUser(userDTO.getUsername(), userDTO.getRoleId(), pageable);
            for (UserModel u : userModels.getContent()) {
                //add report number and ban available
                if(u.getRole().getId() == 2){
                    reportNumber = postRepository.getReportNumberOfLandlord(u.getUsername());
                    u.getLandlordModel().setReportNumber(reportNumber != null ? reportNumber : 0);
                    u.getLandlordModel().setBanAvailable(reportNumber != null ? reportNumber >= Constant.NUMBER_OF_BAN_DATE_USER : false);
                }
            }
            JSONObject pagination = paginationModel(userModels);
            JSONObject resposnse = Constant.responseMsg("000", "Success", userModels);
            resposnse.put("pagination", pagination);
            return resposnse;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("777", "Lỗi dữ liệu!", null);
        }
    }
}
