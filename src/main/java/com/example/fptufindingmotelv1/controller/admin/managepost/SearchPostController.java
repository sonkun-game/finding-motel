package com.example.fptufindingmotelv1.controller.admin.managepost;

import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.service.admin.managepost.SearchPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class SearchPostController {

    @Autowired
    Environment env;

    @Autowired
    SearchPostService searchPostService;

    @ResponseBody
    @RequestMapping(value = "/search-post")
    public JSONObject searchPost(@RequestBody PostSearchDTO postSearchDTO, @RequestParam Optional<Integer> currentPage) {
        try {
            Integer pageSize = new Integer(env.getProperty("ffm.pagination.pageSize"));
            Pageable pageable = PageRequest.of(currentPage.orElse(0), pageSize, Sort.by("createDate").descending());
            return searchPostService.searchPost(postSearchDTO, pageable);
        } catch (Exception e) {
            return Constant.responseMsg("999", "Lỗi hệ thống!", null);
        }
    }

}
