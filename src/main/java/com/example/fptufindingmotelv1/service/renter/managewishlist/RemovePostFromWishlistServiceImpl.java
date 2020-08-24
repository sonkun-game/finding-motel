package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RemovePostFromWishlistServiceImpl implements RemovePostFromWishlistService {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Override
    public boolean removeItem(WishListDTO wishListDTO) {
        try {
            if(wishListDTO.isWishListScreen()){
                wishListRepository.deleteWishListById(wishListDTO.getId());
            }else {
                wishListRepository.deleteWishListByPostAndRenter(wishListDTO.getPostId(), wishListDTO.getRenterUsername());
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
