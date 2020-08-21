package com.example.fptufindingmotelv1.controller.renter.managewishlist;

import com.example.fptufindingmotelv1.service.renter.managewishlist.ViewWishlistService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewWishlistController {

    @Autowired
    ViewWishlistService viewWishlistService;

    @ResponseBody
    @PostMapping(value = "/api-get-wishlist")
    public JSONObject getWishlist(){
        return viewWishlistService.getWishlist();
    }
}
