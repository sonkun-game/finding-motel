package com.example.fptufindingmotelv1.controller.renter.managewishlist;

import com.example.fptufindingmotelv1.service.renter.managewishlist.ViewWishlistService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class ViewWishlistController {

    @Autowired
    ViewWishlistService viewWishlistService;

    @Autowired
    Environment env;

    @ResponseBody
    @PostMapping(value = "/api-get-wishlist")
    public JSONObject getWishlist(@RequestParam Optional<Integer> currentPage){
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize);
            return viewWishlistService.getWishlist(pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
