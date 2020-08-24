package com.example.fptufindingmotelv1.controller.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.service.renter.managewishlist.AddPostToWishlistService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AddPostToWishlistController {

    @Autowired
    AddPostToWishlistService addPostToWishlistService;

    @ResponseBody
    @PostMapping(value = "/api-add-wishlist")
    public JSONObject addWishlist(@RequestBody WishListDTO wishListDTO){

        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel().getRole().getId() == Constant.RENTER_ID){
                if (wishListDTO.getRenterUsername() == null || wishListDTO.getRenterUsername().isEmpty()){
                    wishListDTO.setRenterUsername(userDetails.getUsername());
                }
                return addPostToWishlistService.addPostToWishList(wishListDTO);
            }
        }
        return Constant.responseMsg("403", "Not Authentication", null);
    }
}
