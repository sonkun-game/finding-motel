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
public class HideUnHidePostServiceImpl implements HideUnHidePostService {

    @Autowired
    private PaymentPackageRepository paymentPackageRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PaymentPackageModel> getListPaymentPackage(Boolean available) {
        try {
            return paymentPackageRepository.getListPaymentPackage(available);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PostModel changePostStatus(PostRequestDTO postRequestDTO) {
        try {
            PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();
            postModel.setVisible(postRequestDTO.getIsVisible());
            postModel = postRepository.save(postModel);
            return postModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
