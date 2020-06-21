package com.example.fptufindingmotelv1.controller.wishlist;

import com.example.fptufindingmotelv1.dto.LoginDTO;
import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import com.example.fptufindingmotelv1.service.displayall.RenterService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class WishlistController {
    @Autowired
    PostService postService;

    @Autowired
    PostModelRepository postModelRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    RenterService renterService;

    @GetMapping(value = "/view")
    public String getWishlist(Model model){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            model.addAttribute("renter",renterService.findOne(userDetails.getUserModel().getUsername()));
        }
        return "wishlist";
    }

    @ResponseBody
    @PostMapping(value = "/api-add-wishlist")
    public JSONObject addWishlist(@RequestParam(name = "id")String id, @RequestParam(name = "status")String status){
        JSONObject jsonObject = new JSONObject();

        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            RenterModel renterModel = renterService.findOne(userDetails.getUserModel().getUsername());
            PostModel postModel= postService.findOne(Long.valueOf(id));
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
}
