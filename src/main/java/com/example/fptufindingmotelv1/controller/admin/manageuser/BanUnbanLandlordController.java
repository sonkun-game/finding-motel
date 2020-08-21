package com.example.fptufindingmotelv1.controller.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.service.admin.manageuser.BanUnbanLandlordService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BanUnbanLandlordController {
    @Autowired
    BanUnbanLandlordService banUnbanLandlordService;

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

    @ResponseBody
    @RequestMapping(value = "/ban-landlord")
    public JSONObject banLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && !username.isEmpty()) {
                LandlordModel landlordModel = banUnbanLandlordService.banLandlord(username);
                return landlordModel != null
                        ? responseMsg("000", "Success!", null)
                        : responseMsg("999", "Lỗi hệ thống!", null);
            }
            return responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public JSONObject unbanLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && !username.isEmpty()) {
                LandlordModel landlordModel = banUnbanLandlordService.unbanLandlord(username);
                return landlordModel != null
                        ? responseMsg("000", "Success!", null)
                        : responseMsg("999", "Lỗi hệ thống!", null);
            }

            return responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            return responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
