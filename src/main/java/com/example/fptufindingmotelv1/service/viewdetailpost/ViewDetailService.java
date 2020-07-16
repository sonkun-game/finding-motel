package com.example.fptufindingmotelv1.service.viewdetailpost;

import com.example.fptufindingmotelv1.model.PostModel;

import java.util.List;

public interface ViewDetailService {
    List<PostModel> getRelatedPosts(String id);
}
