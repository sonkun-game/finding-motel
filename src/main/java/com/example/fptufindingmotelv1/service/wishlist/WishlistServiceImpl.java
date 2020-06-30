package com.example.fptufindingmotelv1.service.wishlist;

import com.example.fptufindingmotelv1.dto.PostDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService{

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    PostRepository postRepository;

    @Override
    public List<PostDTO> getWishlist() {
        return null;
    }

    @Override
    public List<PostDTO> removeItem(String username, Long postId) {
        try {
            RenterModel renterModel = renterRepository.findByUsername(username);
            PostModel postModel = postRepository.findById(postId).get();
            renterModel.getPosts().remove(postModel);
            renterRepository.save(renterModel);
            List<PostDTO> response = new ArrayList<>();
            for (PostModel post: renterModel.getPosts()) {
                response.add(new PostDTO(post));
            }
            return response;
        }catch (Exception exception){
            System.err.println(exception);
        }
        return null;
    }
}
