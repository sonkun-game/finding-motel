package com.example.fptufindingmotelv1.service.wishlist;

import com.example.fptufindingmotelv1.dto.PostDTO;

import java.util.List;

public interface WishlistService {
    public List<PostDTO> getWishlist();
    public List<PostDTO> removeItem(String username, Long postId);
}
