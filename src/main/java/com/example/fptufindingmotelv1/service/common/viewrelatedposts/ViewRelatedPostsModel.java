package com.example.fptufindingmotelv1.service.common.viewrelatedposts;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ViewRelatedPostsModel implements ViewRelatedPostsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    Environment env;

    @Override
    public List<PostModel> getRelatedPosts(PostRequestDTO postRequestDTO) {
        try {
            Date date = new Date();
            Date currentDate = new Timestamp(date.getTime());
            Integer numberOfRelatedPost = new Integer(env.getProperty("ffm.relatedPost.number"));
            Pageable pageable = PageRequest.of(0, numberOfRelatedPost);

            if(postRequestDTO.getPostId() != null && !postRequestDTO.getPostId().isEmpty()){
                return postRepository.getRelatedPost(postRequestDTO.getPostId(),
                        postRequestDTO.getUsername(), postRequestDTO.getTypeId(),
                        true, false, currentDate, pageable);
            }else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
