package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Pageable;

public interface SearchPostService {


    JSONObject searchPost(PostSearchDTO postSearchDTO, Pageable pageable);
}
