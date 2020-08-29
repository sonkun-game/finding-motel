package com.example.fptufindingmotelv1.controller.admin.manageuser;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.service.admin.manageuser.SearchUserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class SearchUserController {
    @Autowired
    private SearchUserService searchUserService;

    @Autowired
    Environment env;

    @ResponseBody
    @RequestMapping(value = "/api-search-user")
    public JSONObject searchUser(@RequestBody UserDTO userDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            return searchUserService.searchUsers(userDTO, pageable);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
