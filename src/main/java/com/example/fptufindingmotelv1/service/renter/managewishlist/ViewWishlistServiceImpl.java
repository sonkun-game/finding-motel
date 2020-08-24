package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import com.example.fptufindingmotelv1.untils.Constant;
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
public class ViewWishlistServiceImpl implements ViewWishlistService {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Override
    public JSONObject getWishlist() {
        try {
            if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
                CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                if(userDetails.getUserModel().getRole().getId() == Constant.RENTER_ID){
                    Date date = new Date();
                    Date currentDate = new Timestamp(date.getTime());
                    List<WishListModel> wishListPosts = wishListRepository.getWishlistOfRenter(
                            userDetails.getUsername(), true, false, currentDate);

                    List<WishListDTO> wishListDTOS = new ArrayList<>();
                    for (WishListModel wishListModel:
                            wishListPosts) {
                        ImageModel imageModel = imageRepository.getImageById(wishListModel.getWishListPost().getImages().get(0).getId());
                        wishListModel.getWishListPost().getImages().get(0).setFileContent(imageModel.getFileContent());
                        wishListModel.getWishListPost().getImages().get(0).setFileType(imageModel.getFileType());
                        wishListDTOS.add(new WishListDTO(wishListModel));
                    }

                    return wishListPosts != null ?
                            Constant.responseMsg("000", "Success", wishListDTOS) :
                            Constant.responseMsg("999", "Lỗi hệ thống!", null);
                }
            }
            return Constant.responseMsg("403", "Not Login As Renter", null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

}
