package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.PagerModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PostModelRepository;
import com.example.fptufindingmotelv1.service.displayall.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;
@Controller
public class HomeController {
    private static final int BUTTONS_TO_SHOW = 3;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 6;
    private static final int[] PAGE_SIZES = { 6, 12};

    @Autowired
    PostService postService;

    @Autowired
    PostModelRepository postModelRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getHomepage(Model model, HttpServletRequest request,
                         RedirectAttributes redirect,
                         @RequestParam(name = "page"/*, required = false, defaultValue = "0"*/) Optional<Integer> page,
                         @RequestParam(name = "pageSize"/*, required = false, defaultValue = "6"*/)  Optional<Integer> size,
                         @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort){

        //request.getSession().setAttribute("postlist", null);
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("createDate").ascending();
        }

        int evalPageSize = size.orElse(INITIAL_PAGE_SIZE);

        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = PageRequest.of(evalPage, evalPageSize,sortable);
        Page<PostModel> postlist =  postModelRepository.findAll(pageable);
        model.addAttribute("posts",postlist);
        PagerModel pager = new PagerModel(postlist.getTotalPages(),postlist.getNumber(),BUTTONS_TO_SHOW);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);
        return "index";
    }

}
