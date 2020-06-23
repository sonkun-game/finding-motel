package com.example.fptufindingmotelv1.controller;
import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.PagerModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import com.example.fptufindingmotelv1.service.displayall.RenterService;
import com.example.fptufindingmotelv1.untils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

        List<PostDTO> postDTOs= new ArrayList<>();
        PostDTO postDTO= null;
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            CustomUserDetails userDetails = (CustomUserDetails)SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            RenterModel renter =renterService.findOne(userDetails.getUserModel().getUsername());
            for(int j=0;j<postList.getSize();j++){
                postDTO=new PostDTO(postList.getContent().get(j));
                if(renter.getPosts().contains(postList.getContent().get(j))){
                    postDTO.setColor("color: red");
                }else {
                    postDTO.setColor("color: white");
                }
                postDTOs.add(postDTO);
            }
        }else{
            for (int i=0;i<postList.getSize();i++){
                postDTO = new PostDTO(postList.getContent().get(i));
                postDTOs.add(postDTO);
            }
        }
        Page<PostDTO> listDTO = new PageImpl<>(postDTOs);
        model.addAttribute("posts",listDTO);
        PagerModel pager = new PagerModel(listDTO.getTotalPages(),listDTO.getNumber(),Constant.BUTTONS_TO_SHOW);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", Constant.PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "index";
    }
}
