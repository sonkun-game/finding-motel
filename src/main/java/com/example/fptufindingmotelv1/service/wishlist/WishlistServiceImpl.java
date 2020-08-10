package com.example.fptufindingmotelv1.service.wishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService{

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Override
    public JSONObject getWishlist() {
        JSONObject response = new JSONObject();
        try {
            if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
                CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                if(userDetails.getUserModel() instanceof RenterModel){
                    RenterModel renterModel = renterRepository.findByUsername(userDetails.getUsername());
                    Sort sort = Sort.by("createdDate").descending();
                    List<WishListModel> wishListModels = wishListRepository.findAllByWishListRenter(renterModel, sort);
                    List<WishListDTO> wishListDTOS = new ArrayList<>();
                    for (WishListModel wishlist:
                            wishListModels) {
                        wishListDTOS.add(new WishListDTO(wishlist));
                    }
                    response.put("msgCode", wishListModels != null ? "wishlist000" : "wishlist001");
                    response.put("wishList", wishListDTOS);

                    return response;
                }
            }
            response.put("msgCode", "wishlist002");
            response.put("message", "Not Login As Renter");
            return response;
        }catch (Exception exception){
            exception.printStackTrace();
            response.put("msgCode", "sys999");
            return response;
        }
    }

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
