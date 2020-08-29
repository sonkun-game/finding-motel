package com.example.fptufindingmotelv1.service.renter.managewishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.WishListModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ViewWishlistModel implements ViewWishlistService {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Override
    public JSONObject getWishlist(Pageable pageable) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                if (userDetails.getUserModel().getRole().getId() == Constant.RENTER_ID) {
                    Date date = new Date();
                    Date currentDate = new Timestamp(date.getTime());
                    Page<WishListModel> wishListPosts = wishListRepository.getWishlistOfRenter(
                            userDetails.getUsername(), true, false, currentDate, pageable);

                    List<WishListDTO> wishListDTOS = new ArrayList<>();
                    for (WishListModel wishListModel : wishListPosts.getContent()) {
                        ImageModel imageModel = imageRepository.getImageById(wishListModel.getWishListPost().getImages().get(0).getId());
                        wishListModel.getWishListPost().getImages().get(0).setFileContent(imageModel.getFileContent());
                        wishListModel.getWishListPost().getImages().get(0).setFileType(imageModel.getFileType());
                        wishListDTOS.add(new WishListDTO(wishListModel));
                    }

                    JSONObject response = Constant.responseMsg("000", "Success", wishListDTOS);
                    response.put("pagination", Constant.paginationModel(wishListPosts));
                    return response;
                }
            }
            return Constant.responseMsg("403", "Not Login As Renter", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

}
