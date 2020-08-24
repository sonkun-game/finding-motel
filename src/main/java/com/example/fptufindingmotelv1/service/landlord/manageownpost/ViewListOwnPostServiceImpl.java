package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ViewListOwnPostServiceImpl implements ViewListOwnPostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Page<PostModel> getAllPost(PostRequestDTO postRequestDTO, Pageable pageable) {
        try {
            return postRepository.getPostsByLandlord(postRequestDTO.getUsername(), pageable);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
