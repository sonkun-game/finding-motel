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
    public List<WishListDTO> removeItem(WishListDTO wishListDTO) {
        try {
            List<WishListDTO> wishListDTOS = new ArrayList<>();
            RenterModel renterModel = new RenterModel(wishListDTO.getRenterUsername());
            if(wishListDTO.isWishListScreen()){
                WishListModel wishListModel = wishListRepository.findById(wishListDTO.getId()).get();
                wishListRepository.delete(wishListModel);
                Sort sort = Sort.by("createdDate").descending();
                List<WishListModel> wishListModels = wishListRepository.findAllByWishListRenter(renterModel, sort);
                for (WishListModel wishlist:
                        wishListModels) {
                    wishListDTOS.add(new WishListDTO(wishlist));
                }
            }else {
                PostModel postModel = new PostModel(wishListDTO.getPostId());
                WishListModel wishListModel = wishListRepository.findByWishListPostAndWishListRenter(postModel, renterModel);
                wishListRepository.delete(wishListModel);
            }
            return wishListDTOS;
        }catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
