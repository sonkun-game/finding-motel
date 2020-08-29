package com.example.fptufindingmotelv1.controller.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.service.renter.managewishlist.RemovePostFromWishlistService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class RemovePostFromWishlistController {

    @Autowired
    RemovePostFromWishlistService removePostFromWishlistService;

    @ResponseBody
    @PostMapping(value = "/api-remove-from-wishlist")
    public JSONObject removeFromWishList(@RequestBody WishListDTO wishListDTO){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (wishListDTO.getRenterUsername() == null || wishListDTO.getRenterUsername().isEmpty()){
                wishListDTO.setRenterUsername(userDetails.getUsername());
            }
            boolean result = removePostFromWishlistService.removeItem(wishListDTO);
            return result ? Constant.responseMsg("000", "Success", null)
                    : Constant.responseMsg("999", "Lỗi hệ thống", null);
        }
        return Constant.responseMsg("403", "Not Authentication", null);
    }
}
