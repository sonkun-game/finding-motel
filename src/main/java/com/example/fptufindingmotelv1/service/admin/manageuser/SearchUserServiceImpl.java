package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchUserServiceImpl implements SearchUserService {

    @Autowired
    UserRepository userRepository;

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

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
            List<UserDTO> users = new ArrayList<>();
            Page<UserModel> userModels = userRepository.searchUser(userDTO.getUsername(), userDTO.getRoleId(), pageable);
            for (UserModel u : userModels.getContent()) {
                UserDTO user = new UserDTO(u);
                //add report number and ban available
                if (u.getRole().getId() == 2) {
                    boolean banAvailable = user.getUnBanDate() == null
                            && user.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_USER;
                    user.setBanAvailable(banAvailable);
                }
                users.add(user);
            }
            JSONObject pagination = paginationModel(userModels);
            JSONObject resposnse = responseMsg("000", "Success", users);
            resposnse.put("pagination", pagination);
            return resposnse;
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("777", "Lỗi dữ liệu!", null);
        }
    }
}
