package com.example.fptufindingmotelv1.service.viewdetailpost;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ViewDetailServiceImpl implements ViewDetailService{

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostModel> getRelatedPosts(String id) {
        try {
            PostModel postModel = postRepository.findById(id).get();
            if(postModel != null){
//                return postRepository.getRelatedPost(postModel.getId(),
//                        postModel.getLandlord().getUsername(), postModel.getType().getId());
                return new ArrayList<>();
            }else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
