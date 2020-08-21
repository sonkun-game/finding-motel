package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
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

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewWishlistServiceImpl implements ViewWishlistService {

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

}
