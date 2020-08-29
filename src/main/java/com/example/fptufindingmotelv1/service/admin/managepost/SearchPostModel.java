package com.example.fptufindingmotelv1.service.admin.managepost;

import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.model.FilterPostModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.FilterPostRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Service
public class SearchPostModel implements SearchPostService{
    @Autowired
    PostRepository postRepository;

    @Autowired
    FilterPostRepository filterPostRepository;

    @Override
    public JSONObject searchPost(PostSearchDTO postSearchDTO, Pageable pageable) {
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
            Boolean visible = null, banned = null;
            Date currentDate = null;
            if(postSearchDTO.getStatusId() != null && postSearchDTO.getStatusId().equals("1")){
                visible = false;
            }else if(postSearchDTO.getStatusId() != null && postSearchDTO.getStatusId().equals("2")){
                visible = true;
            }else if(postSearchDTO.getStatusId() != null && postSearchDTO.getStatusId().equals("3")){
                banned = true;
            }else if(postSearchDTO.getStatusId() != null && postSearchDTO.getStatusId().equals("4")){
                Date date = new Date();
                currentDate = new Timestamp(date.getTime());
            }

            Page<PostModel> posts = postRepository.searchPost(postSearchDTO.getLandlordUsername(),
                    postSearchDTO.getTitle(), maxPrice, minPrice, maxDistance, minDistance,
                    maxSquare, minSquare, visible,
                    postSearchDTO.getTypeId(), banned, currentDate, pageable);
            JSONObject response = Constant.responseMsg("000", "Successs", posts);
            response.put("pagination", paginationModel(posts));
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.responseMsg("777", "Lỗi dữ liệu.", null);
        }
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
