package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.dto.TypePostDTO;
import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.service.landlord.ManagePostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    InstructionRepository instructionRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    ManagePostService managePostService;

    @Autowired
    FilterPostRepository filterPostRepository;

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(@RequestParam(name = "pages", defaultValue = "1") Optional<Integer> page){
        return "index";
    }

    @GetMapping("/huong-dan")
    public String viewInstruction(Model model) {
        return "instruction";
    }

    @ResponseBody
    @GetMapping("/api-get-list-instruction")
    public JSONObject getListInstruction(){
        JSONObject response = new JSONObject();
        try {
            List<InstructionModel> instructions = instructionRepository.getAllInstruction();
            response.put("code", "000");
            response.put("data", instructions);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống");
            return response;
        }
    }

    @ResponseBody
    @GetMapping("/api-get-init-home-page")
    public JSONObject getInitHomePage(){
        JSONObject response = new JSONObject();
        try {
            //get Post type
            List<TypeModel> typePosts = managePostService.getListTypePost();
            if(typePosts != null){
                List<TypePostDTO> typePostDTOS = new ArrayList<>();
                for (TypeModel typeModel:
                        typePosts) {
                    typePostDTOS.add(new TypePostDTO(typeModel));
                }
                response.put("listTypePost", typePostDTOS);
            }
            // get List filter post
            // get List Price
            List<FilterPostModel> listFilterPrice = filterPostRepository.findAllByType("PRICE");
            if(listFilterPrice != null){
                response.put("listFilterPrice", listFilterPrice);
            }
            // get List Square
            List<FilterPostModel> listFilterSquare = filterPostRepository.findAllByType("SQUARE");
            if(listFilterSquare != null){
                response.put("listFilterSquare", listFilterSquare);
            }
            // get List Distance
            List<FilterPostModel> listFilterDistance = filterPostRepository.findAllByType("DISTANCE");
            if(listFilterDistance != null){
                response.put("listFilterDistance", listFilterDistance);
            }
            response.put("msgCode", "home000");

            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.put("msgCode", "sys999");
            return response;
        }
    }

    @ResponseBody
    @PostMapping("/filter-post")
    public JSONObject filterPost(@RequestBody PostSearchDTO postSearchDTO){
        JSONObject response = new JSONObject();
        try {
            Double minPrice = null;
            Double maxPrice = null;
            Double minSquare = null;
            Double maxSquare = null;
            Double minDistance = null;
            Double maxDistance = null;
            if(postSearchDTO.getFilterPriceId() != null){
                FilterPostModel filterPrice = filterPostRepository.findById(postSearchDTO.getFilterPriceId()).get();
                minPrice = filterPrice.getMinValue();
                maxPrice = filterPrice.getMaxValue();
            }
            if(postSearchDTO.getFilterSquareId() != null){
                FilterPostModel filterSquare = filterPostRepository.findById(postSearchDTO.getFilterSquareId()).get();
                minSquare = filterSquare.getMinValue();
                maxSquare = filterSquare.getMaxValue();
            }
            if(postSearchDTO.getFilterDistanceId() != null){
                FilterPostModel filterDistance = filterPostRepository.findById(postSearchDTO.getFilterDistanceId()).get();
                minDistance = filterDistance.getMinValue();
                maxDistance = filterDistance.getMaxValue();
            }

            ArrayList<PostModel> listPostModel = (ArrayList<PostModel>) postRepository.filterPost(null,
                    null, maxPrice, minPrice,
                    maxDistance, minDistance,
                    maxSquare, minSquare, true, postSearchDTO.getTypeId(), false);


            // Paging
            int evalPageSize = Constant.INITIAL_PAGE_SIZE;
            int evalPage = (postSearchDTO.getPage().orElse(0) < 1) ? Constant.INITIAL_PAGE : postSearchDTO.getPage().get() - 1;

            Pageable pageable = PageRequest.of(evalPage, evalPageSize);

            int total = listPostModel.size();
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), total);

            List<PostModel> sublistPostModel = new ArrayList<>();
            if (start <= end) {
                sublistPostModel = listPostModel.subList(start, end);
            }
            List<PostDTO> postDTOs = new ArrayList<>();
            PostDTO postDTO;
            for (PostModel post:
                 sublistPostModel) {
                ImageModel imageModel = imageRepository.getImageById(post.getImages().get(0).getId());
                post.getImages().get(0).setFileContent(imageModel.getFileContent());
                post.getImages().get(0).setFileType(imageModel.getFileType());
                postDTO = new PostDTO();
                postDTO.setPostDTO(post);
                postDTOs.add(postDTO);
            }
//            List<PostDTO> postDTOs = new ArrayList<>();
//            PostDTO postDTO;
//            // check login
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth instanceof UsernamePasswordAuthenticationToken
//                    && ((CustomUserDetails)auth.getPrincipal()).getUserModel() instanceof RenterModel) {
//                RenterModel renter = renterRepository.findByUsername(((CustomUserDetails)auth.getPrincipal()).getUserModel().getUsername());
//                for (PostModel post:
//                        sublistPostModel) {
//                    postDTO = new PostDTO(post);
//                    WishListModel wishListModel = wishListRepository.findByWishListPostAndWishListRenter(post, renter);
//                    if(wishListModel != null){
//                        postDTO.setInWishList(true);
//                        postDTO.setWishListId(wishListModel.getId());
//                    }else {
//                        postDTO.setInWishList(false);
//                    }
//                    postDTOs.add(postDTO);
//                }
//            } else {
//                for (PostModel post:
//                        sublistPostModel) {
//                    postDTO = new PostDTO(post);
//                    postDTO.setInWishList(false);
//                    postDTOs.add(postDTO);
//                }
//            }

            Page<PostDTO> listDTO = new PageImpl<>(postDTOs, pageable, listPostModel.size());
            response.put("pageSize", evalPageSize);
            response.put("page", listDTO);

            PagerModel pager = new PagerModel(listDTO.getTotalPages(), listDTO.getNumber(), Constant.BUTTONS_TO_SHOW);
            response.put("endPage", pager.getEndPage());
            response.put("pager", pager);
            response.put("msgCode", "home000");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msgCode", "sys999");
            return response;
        }
    }

    @ResponseBody
    @RequestMapping("/api-get-wish-list")
    public JSONObject getWishListPost(@RequestBody WishListDTO wishListDTO) {
        JSONObject response = new JSONObject();
        try {
            List<PostModel> listPostWishList = wishListRepository.getListPostByRenter(
                    wishListDTO.getRenterUsername(), true, false);
            List<PostDTO> postDTOs = new ArrayList<>();
            PostDTO postDTO;
            for (PostModel post:
                    listPostWishList) {
                postDTOs.add(new PostDTO(post.getId()));
            }
            response.put("code", "000");
            response.put("data", postDTOs);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi Hệ Thống");
            return response;
        }
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

    @GetMapping(value = {"/phong-tro", "/can-ho"})
    public String getTypePostPage() {
        return "type-post";
    }
}
