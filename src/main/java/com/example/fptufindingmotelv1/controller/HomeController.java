package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.repository.WishListRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Controller
public class HomeController {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    WishListRepository wishListRepository;

    @ResponseBody
    @PostMapping(value = {"/api-get-posts"})
    public JSONObject getPosts(@RequestParam(name = "page", defaultValue = "1") Optional<Integer> page,
                             @RequestParam(name = "pageSize")  Optional<Integer> size,
                             @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort){

        JSONObject jsonObject= new JSONObject();
        Sort sortable = null;
        if (sort.equals("DESC")) {
            sortable = Sort.by("createDate").descending();
        }

        // Paging
        int evalPageSize = size.orElse(Constant.INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;

        Pageable pageable = PageRequest.of(evalPage, evalPageSize, sortable);
        List<PostModel> listPostModel =  postRepository.findByVisibleTrueAndBannedFalse(sortable);

        int total = listPostModel.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<PostModel> sublistPostModel = new ArrayList<>();
        if (start <= end) {
            sublistPostModel = listPostModel.subList(start, end);
        }

        List<PostDTO> postDTOs = new ArrayList<>();
        PostDTO postDTO;
        // check login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken
                && ((CustomUserDetails)auth.getPrincipal()).getUserModel() instanceof RenterModel) {
            RenterModel renter = renterRepository.findByUsername(((CustomUserDetails)auth.getPrincipal()).getUserModel().getUsername());
            for (PostModel post:
                    sublistPostModel) {
                postDTO = new PostDTO(post);
                WishListModel wishListModel = wishListRepository.findByWishListPostAndWishListRenter(post, renter);
                if(wishListModel != null){
                    postDTO.setInWishList(true);
                    postDTO.setWishListId(wishListModel.getId());
                }else {
                    postDTO.setInWishList(false);
                }
                postDTOs.add(postDTO);
            }
        } else {
            for (PostModel post:
                    sublistPostModel) {
                postDTO = new PostDTO(post);
                postDTO.setInWishList(false);
                postDTOs.add(postDTO);
            }
        }

        Page<PostDTO> listDTO = new PageImpl<>(postDTOs, pageable, listPostModel.size());
        jsonObject.put("pageSize",evalPageSize);
        jsonObject.put("page",listDTO);

        PagerModel pager = new PagerModel(listDTO.getTotalPages(), listDTO.getNumber(), Constant.BUTTONS_TO_SHOW);
        jsonObject.put("endPage",pager.getEndPage());
        jsonObject.put("pager",pager);
        return jsonObject;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(@RequestParam(name = "pages", defaultValue = "1") Optional<Integer> page){
        return "index";
    }

    @GetMapping("/post-detail")
    public String getPostDetail() {
        return "post-detail";
    }

    @ResponseBody
    @PostMapping(value = "/api-post-detail")
    public PostDTO viewPost(@PathParam("id") String id){
        PostModel postModel = postRepository.findById(id).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof UsernamePasswordAuthenticationToken
                && ((CustomUserDetails)auth.getPrincipal()).getUserModel() instanceof RenterModel) {
            RenterModel renter = renterRepository.findByUsername(((CustomUserDetails)auth.getPrincipal()).getUserModel().getUsername());
            WishListModel wishListModel = wishListRepository.findByWishListPostAndWishListRenter(postModel, renter);
            PostDTO response = new PostDTO(postModel);

            if(wishListModel != null){
                response.setInWishList(true);
                response.setWishListId(wishListModel.getId());
            }else {
                response.setInWishList(false);
            }
            return response;
        } else {
            PostDTO response = new PostDTO(postModel);
            response.setInWishList(false);
            return response;
        }

    }

    @GetMapping("/huong-dan")
    public String viewInstruction(Model model) {
        model.addAttribute("customer",roleRepository.getOne((long) 2));
        return "instruction";
    }

    @ResponseBody
    @GetMapping("/api-test-renter")
    public String testRenter() {
        return "Renter Access";
    }

    @ResponseBody
    @GetMapping("/api-test-landlord")
    public String testLandlord() {
        return "Landlord Access";
    }

    @ResponseBody
    @GetMapping("/api-test-admin")
    public String testAdmin() {
        return "Admin Access";
    }

    @GetMapping("/loai-phong")
    public String getType() {
        return "type-room";
    }
}
