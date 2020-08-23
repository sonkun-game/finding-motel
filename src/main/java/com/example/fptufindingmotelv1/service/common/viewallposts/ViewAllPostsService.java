package com.example.fptufindingmotelv1.service.common.viewallposts;

import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface ViewAllPostsService {
    JSONObject getFilterPost();
    List<PostModel> getListPost(PostSearchDTO postSearchDTO);
    List<PostModel> getListPostByRenter(WishListDTO wishListDTO);
}
