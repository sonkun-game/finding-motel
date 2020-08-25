package com.example.fptufindingmotelv1.service.common.viewinformationofapost;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import com.example.fptufindingmotelv1.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ViewInformationOfAPostServiceImpl implements ViewInformationOfAPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<RoomModel> getListRoomOfPost(String postId) {
        try {
            return  roomRepository.getListRoomByPostId(postId);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PostModel getPostDetail(String id) {
        try {
            PostModel postModel = postRepository.getPostById(id);
            Date date = new Date();
            Date currentDate = new Timestamp(date.getTime());
            if(postModel.getExpireDate().before(currentDate) && postModel.isVisible()){
                postModel.setVisible(false);
                postRepository.updateVisiblePost(false, postModel.getId());
            }
            postModel.setImages(imageRepository.getImageByPostId(id));
            return postModel;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
