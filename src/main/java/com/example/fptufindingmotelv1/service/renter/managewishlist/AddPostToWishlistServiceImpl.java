package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class AddPostToWishlistServiceImpl implements AddPostToWishlistService {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Override
    public JSONObject addPostToWishList(WishListDTO wishListDTO) {
        JSONObject response = new JSONObject();
        try {
            RenterModel renterModel = new RenterModel(wishListDTO.getRenterUsername());
            PostModel postModel = new PostModel(wishListDTO.getPostId());
            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            WishListModel wishListModel = new WishListModel();
            wishListModel.setWishListRenter(renterModel);
            wishListModel.setWishListPost(postModel);
            wishListModel.setCreatedDate(createdDate);
            wishListModel = wishListRepository.save(wishListModel);
            response.put("msgCode", wishListModel != null ? "wishlist000" : "sys999");
//            response.put("wishList", new WishListDTO(wishListModel));
            return response;
        }catch (Exception exception){
            exception.printStackTrace();
            response.put("msgCode", "sys999");
            return response;
        }
    }
}
