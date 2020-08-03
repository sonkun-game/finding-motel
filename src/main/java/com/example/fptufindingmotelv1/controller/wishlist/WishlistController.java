package com.example.fptufindingmotelv1.controller.wishlist;

import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.service.wishlist.WishlistService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class WishlistController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    WishlistService wishlistService;

    @ResponseBody
    @PostMapping(value = "/api-add-wishlist")
    public JSONObject addWishlist(@RequestBody WishListDTO wishListDTO){
        JSONObject response = new JSONObject();

        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if(userDetails.getUserModel() instanceof RenterModel){
                if (wishListDTO.getRenterUsername() == null || wishListDTO.getRenterUsername().isEmpty()){
                    wishListDTO.setRenterUsername(userDetails.getUsername());
                }
                return wishlistService.addPostToWishList(wishListDTO);
            }
        }
        response.put("msgCode", "wishlist002");
        response.put("message", "Not Login As Renter");
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/api-get-wishlist")
    public JSONObject getWishlist(){
        return wishlistService.getWishlist();
    }

    @ResponseBody
    @PostMapping(value = "/api-remove-from-wishlist")
    public JSONObject removeFromWishList(@RequestBody WishListDTO wishListDTO){
        JSONObject response = new JSONObject();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            if (wishListDTO.getRenterUsername() == null || wishListDTO.getRenterUsername().isEmpty()){
                wishListDTO.setRenterUsername(userDetails.getUsername());
            }
            List<WishListDTO> wishListDTOS = wishlistService.removeItem(wishListDTO);
            response.put("msgCode", wishListDTOS != null ? "wishlist000" : "wishlist001");
            response.put("wishList", wishListDTOS);
            return response;
        }
        return null;
    }
}
