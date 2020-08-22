package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import net.minidev.json.JSONObject;


public interface AddPostToWishlistService {

    JSONObject addPostToWishList(WishListDTO wishListDTO);
}
