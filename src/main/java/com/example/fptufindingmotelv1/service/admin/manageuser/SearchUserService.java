package com.example.fptufindingmotelv1.service.admin.manageuser;

import com.example.fptufindingmotelv1.dto.UserDTO;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface SearchUserService {
    JSONObject searchUsers(UserDTO userDTO, Pageable pageable);
}
