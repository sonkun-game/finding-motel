package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HideUnHidePostModel implements HideUnHidePostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public boolean changePostStatus(PostRequestDTO postRequestDTO) {
        try {
            postRepository.updateVisiblePost(postRequestDTO.getIsVisible(), postRequestDTO.getPostId());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
