package com.example.fptufindingmotelv1.service.common.viewrelatedposts;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViewRelatedPostsServiceImpl implements ViewRelatedPostsService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<PostModel> getRelatedPosts(PostRequestDTO postRequestDTO) {
        try {

            if(postRequestDTO.getPostId() != null && !postRequestDTO.getPostId().isEmpty()){
                return postRepository.getRelatedPost(postRequestDTO.getPostId(),
                        postRequestDTO.getUsername(), postRequestDTO.getTypeId(),
                        true, false);
            }else{
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
