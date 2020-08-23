package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SearchPostServiceImpl implements SearchPostService{
    @Autowired
    PostRepository postRepository;

    @Override
    public JSONObject searchPost(PostSearchDTO postSearchDTO, Pageable pageable) {
        try {
            Page<PostModel> posts = postRepository.searchPost(postSearchDTO.getLandlordUsername(),
                    postSearchDTO.getTitle(), postSearchDTO.getPriceMax(), postSearchDTO.getPriceMin(),
                    postSearchDTO.getDistanceMax(), postSearchDTO.getDistanceMin(),
                    postSearchDTO.getSquareMax(), postSearchDTO.getSquareMin(), postSearchDTO.getVisible(),
                    postSearchDTO.getTypeId(), null, pageable);
            ArrayList<PostResponseDTO> postResponseDTOs = new ArrayList<>();
            for (PostModel p : posts.getContent()) {
                PostResponseDTO pr = new PostResponseDTO(p);
                boolean banAvailable = pr.getReportNumber() >= Constant.NUMBER_OF_BAN_DATE_POST;
                pr.setBanAvailable(banAvailable);
                postResponseDTOs.add(pr);
            }
            JSONObject response = responseMsg("000", "Successs", postResponseDTOs);
            response.put("pagination", paginationModel(posts));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return responseMsg("777", "Lỗi dữ liệu.", null);
        }
    }
    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }

    public JSONObject paginationModel(Page page) {
        JSONObject msg = new JSONObject();
        msg.put("totalPages", page.getTotalPages());
        msg.put("sizePage", page.getSize());
        msg.put("currentPage", page.getNumber());
        msg.put("totalItems", page.getTotalElements());
        msg.put("hasNext", page.hasNext());
        msg.put("hasPrevious", page.hasPrevious());
        return msg;
    }
}
