package com.example.fptufindingmotelv1.service.displayall;

import com.example.fptufindingmotelv1.model.PostModel;

import java.util.List;

public interface PostService {
    List<PostModel> findAll();
    PostModel findOne(Long id);

}
