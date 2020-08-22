package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ViewListOwnPostServiceImpl implements ViewListOwnPostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostModel> getAllPost(PostRequestDTO postRequestDTO) {
        try {
            return postRepository.getPostsByLandlord(postRequestDTO.getUsername());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
