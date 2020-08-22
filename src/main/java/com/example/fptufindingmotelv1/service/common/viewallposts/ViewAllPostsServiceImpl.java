package com.example.fptufindingmotelv1.service.common.viewallposts;

import com.example.fptufindingmotelv1.dto.PostSearchDTO;
import com.example.fptufindingmotelv1.dto.WishListDTO;
import com.example.fptufindingmotelv1.model.FilterPostModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.repository.FilterPostRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.TypeRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ViewAllPostsServiceImpl implements ViewAllPostsService{

    @Autowired
    FilterPostRepository filterPostRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public JSONObject getFilterPost() {
        JSONObject response = new JSONObject();
        try {
            //get Post type
            List<TypeModel> typePosts = typeRepository.getAllTypeOfPost();
            response.put("listTypePost", typePosts);

            // get List filter post
            // get List Price
            List<FilterPostModel> listFilterPrice = filterPostRepository.findAllByType("PRICE");
            response.put("listFilterPrice", listFilterPrice);

            // get List Square
            List<FilterPostModel> listFilterSquare = filterPostRepository.findAllByType("SQUARE");
            response.put("listFilterSquare", listFilterSquare);

            // get List Distance
            List<FilterPostModel> listFilterDistance = filterPostRepository.findAllByType("DISTANCE");
            response.put("listFilterDistance", listFilterDistance);
            response.put("code", "000");

            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống!");
            return response;
        }
    }

    @Override
    public List<PostModel> getListPost(PostSearchDTO postSearchDTO) {
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
            Date date = new Date();
            Date currentDate = new Timestamp(date.getTime());

            return postRepository.filterPost(null,
                    null, maxPrice, minPrice,
                    maxDistance, minDistance,
                    maxSquare, minSquare, true, postSearchDTO.getTypeId(), false, currentDate);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<PostModel> getListPostByRenter(WishListDTO wishListDTO) {
        try {
            Date date = new Date();
            Date currentDate = new Timestamp(date.getTime());
            return postRepository.getListPostByRenter(
                    wishListDTO.getRenterUsername(), true, false, currentDate);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
