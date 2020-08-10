package com.example.fptufindingmotelv1.service.viewdetailpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;

import java.util.List;

public interface ViewDetailService {
    List<PostModel> getRelatedPosts(PostRequestDTO postRequestDTO);

    List<RoomModel> getListRoomOfPost(String postId);

    PostModel getPostDetail(String id);
}
