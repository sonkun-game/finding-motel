package com.example.fptufindingmotelv1.controller.admin.manageuser;

import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.service.admin.manageuser.BanUnbanLandlordService;
import com.example.fptufindingmotelv1.untils.Constant;
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
                boolean isSuccess = banUnbanLandlordService.banLandlord(username);
                return isSuccess
                        ? Constant.responseMsg("000", "Success!", null)
                        : Constant.responseMsg("999", "Lỗi hệ thống!", null);
            }
            return Constant.responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/unban-landlord")
    public JSONObject unbanLandlord(@RequestParam(value = "username") String username) {
        try {
            if (username != null && !username.isEmpty()) {
                boolean isSuccess = banUnbanLandlordService.unbanLandlord(username);
                return isSuccess
                        ? Constant.responseMsg("000", "Success!", null)
                        : Constant.responseMsg("999", "Lỗi hệ thống!", null);
            }

            return Constant.responseMsg("001", "Sai tên đăng nhập chủ trọ", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
