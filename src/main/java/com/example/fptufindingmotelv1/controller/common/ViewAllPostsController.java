package com.example.fptufindingmotelv1.controller.common;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.service.common.viewallposts.ViewAllPostsService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Controller
public class ViewAllPostsController {

    @Autowired
    ViewAllPostsService viewAllPostsService;

    @Autowired
    ImageRepository imageRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(@RequestParam(name = "pages", defaultValue = "1") Optional<Integer> page){
        return "index";
    }

    @ResponseBody
    @GetMapping("/api-get-init-home-page")
    public JSONObject getInitHomePage(){
        return viewAllPostsService.getFilterPost();
    }

    @ResponseBody
    @PostMapping("/filter-post")
    public JSONObject filterPost(@RequestBody PostSearchDTO postSearchDTO){
        JSONObject response = new JSONObject();
        try {
            List<PostModel> listPostModel = viewAllPostsService.getListPost(postSearchDTO);
            if(listPostModel == null){
                response.put("code", "999");
                response.put("message", "Lỗi hệ thống!");
                return response;
            }

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

            Page<PostDTO> listDTO = new PageImpl<>(postDTOs, pageable, listPostModel.size());
            response.put("pageSize", evalPageSize);
            response.put("page", listDTO);

            PagerModel pager = new PagerModel(listDTO.getTotalPages(), listDTO.getNumber(), Constant.BUTTONS_TO_SHOW);
            response.put("endPage", pager.getEndPage());
            response.put("pager", pager);
            response.put("code", "000");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống!");
            return response;
        }
    }

    @ResponseBody
    @RequestMapping("/api-get-wish-list")
    public JSONObject getWishListPost(@RequestBody WishListDTO wishListDTO) {
        JSONObject response = new JSONObject();
        try {
            List<PostModel> listPostWishList = viewAllPostsService.getListPostByRenter(wishListDTO);
            response.put("code", "000");
            response.put("data", listPostWishList);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi Hệ Thống");
            return response;
        }
    }

    @GetMapping(value = {"/phong-tro", "/can-ho"})
    public String getTypePostPage() {
        return "type-post";
    }
}
