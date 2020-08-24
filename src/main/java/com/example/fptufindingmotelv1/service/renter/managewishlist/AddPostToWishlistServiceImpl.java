package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import com.example.fptufindingmotelv1.untils.Constant;
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
        try {
            RenterModel renterModel = new RenterModel(wishListDTO.getRenterUsername());
            PostModel postModel = new PostModel(wishListDTO.getPostId());
            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());

            WishListModel wishListModel = new WishListModel();
            wishListModel.setWishListRenter(renterModel);
            wishListModel.setWishListPost(postModel);
            wishListModel.setCreatedDate(createdDate);
            wishListRepository.save(wishListModel);
            return Constant.responseMsg("000", "Success", null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }
}
