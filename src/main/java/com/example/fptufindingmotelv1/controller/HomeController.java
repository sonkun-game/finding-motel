package com.example.fptufindingmotelv1.controller;
import com.example.fptufindingmotelv1.model.PagerModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import com.example.fptufindingmotelv1.service.displayall.RenterService;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
@Controller
public class HomeController {

    @Autowired
    PostService postService;

    @Autowired
    PostModelRepository postModelRepository;

    @Autowired
    RenterService renterService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(Model model,
                         @RequestParam(name = "page") Optional<Integer> page,
                         @RequestParam(name = "pageSize")  Optional<Integer> size,
                         @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){

        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("createDate").ascending();
        }

        int evalPageSize = size.orElse(Constant.INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? Constant.INITIAL_PAGE : page.get() - 1;
        Pageable pageable = PageRequest.of(evalPage, evalPageSize,sortable);
        Page<PostModel> postList =  postModelRepository.findAll(pageable);

        /*List<WishlistDTO> wishListDTOs= new ArrayList<>();
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            RenterModel renter =renterService.findOne(userDetails.getUserModel().getUsername());
            WishlistDTO wishlistDTO;
            for(int j=0;j<postList.getSize();j++){
                wishlistDTO=new WishlistDTO(postList.getContent().get(j));
                for(int i=0;i< renter.getPosts().size();i++){
                    if(postList.getContent().get(j).getId() == renter.getPosts().get(i).getId()){
                        wishlistDTO.setColor("color: red");
                    }else {
                        wishlistDTO.setColor("color: white");
                    }
                }
                wishListDTOs.add(wishlistDTO);
            }
        }
        Page<WishlistDTO> listDTO = new PageImpl<>(wishListDTOs);*/
        model.addAttribute("posts",postList);
        PagerModel pager = new PagerModel(postList.getTotalPages(),postList.getNumber(),Constant.BUTTONS_TO_SHOW);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", Constant.PAGE_SIZES);
        model.addAttribute("pager", pager);

        return "index";
    }

    @GetMapping("/forgot")
    public String getForgot(Model model){
        return "forgot";
    }
    @GetMapping("/reset-password")
    public String getResetPassword(Model model){
        return "reset-password";
    }
    @GetMapping("/profile-landlord")
    public String getProfileLandlord(Model model){
        return "profile-landlord";
    }
    @GetMapping("/profile-renter")
    public String getProfileRenter(Model model){
        return "profile-renter";
    }
    @GetMapping("/profile-admin")
    public String getProfileAdmin(Model model){
        return "profile-admin";
    }
    @GetMapping("/post-detail")
    public String getPostDetail(Model model){
        return "post-detail";
    }
}
