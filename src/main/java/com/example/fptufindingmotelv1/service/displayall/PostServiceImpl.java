package com.example.fptufindingmotelv1.service.displayall;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Override
    public List<PostModel> findAll() {
        return postRepository.findAll();
    }

    @Override
    public PostModel findOne(String id) {
        return postRepository.findById(id).get();
    }
}
