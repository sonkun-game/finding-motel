package com.example.fptufindingmotelv1.service.wishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import net.minidev.json.JSONObject;

import java.util.List;

public interface WishlistService {
    JSONObject getWishlist();
    List<WishListDTO> removeItem(WishListDTO wishListDTO);

    JSONObject addPostToWishList(WishListDTO wishListDTO);
}
