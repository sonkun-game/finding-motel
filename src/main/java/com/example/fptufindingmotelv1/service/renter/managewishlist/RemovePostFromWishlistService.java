package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;

import java.util.List;

public interface RemovePostFromWishlistService {
    List<WishListDTO> removeItem(WishListDTO wishListDTO);

}
