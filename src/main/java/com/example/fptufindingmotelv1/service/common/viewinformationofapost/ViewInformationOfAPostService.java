package com.example.fptufindingmotelv1.service.common.viewinformationofapost;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;

import java.util.List;

public interface ViewInformationOfAPostService {
    List<RoomModel> getListRoomOfPost(String postId);

    PostModel getPostDetail(String id);
}
