package com.example.fptufindingmotelv1.controller.wishlist;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import com.example.fptufindingmotelv1.service.displayall.RenterService;
import com.example.fptufindingmotelv1.service.wishlist.WishlistService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WishlistController {
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    RenterService renterService;

    @Autowired
    WishlistService wishlistService;

    @ResponseBody
    @PostMapping(value = "/api-add-wishlist")
    public JSONObject addWishlist(@RequestParam(name = "id")String id, @RequestParam(name = "status")String status){
        JSONObject jsonObject = new JSONObject();

        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            RenterModel renterModel = renterService.findOne(userDetails.getUserModel().getUsername());
            PostModel postModel= postService.findOne(id);
            if(status.equals("add")){
                renterModel.getPosts().add(postModel);
            } else if(status.equals("remove")){
                renterModel.getPosts().remove(postModel);
            }
            renterRepository.save(renterModel);
        }
        jsonObject.put("msg","cái j");
        return jsonObject;
    }

    @ResponseBody
    @PostMapping(value = "/api-get-wishlist")
    public List<PostDTO> getWishlist(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            RenterModel renterModel = renterService.findOne(userDetails.getUsername());
            List<PostDTO> response = new ArrayList<>();
            for (PostModel post: renterModel.getPosts()) {
                response.add(new PostDTO(post));
            }
            return response;
        }
        return null;
    }

    @ResponseBody
    @GetMapping(value = "/api-remove-from-wishlist")
    public List<PostDTO> removeFromWishList(@RequestParam String postId){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return wishlistService.removeItem(userDetails.getUsername(), postId);
        }
        return null;
    }
}
