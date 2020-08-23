package com.example.fptufindingmotelv1.service.landlord.manageroom;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddRoomServiceImpl implements AddRoomService {

    @Autowired
    private PostRepository postRepository;


    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<RoomModel> increaseRoom(PostRequestDTO postRequestDTO) {
        try {
            PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();
            // save list room
            List<RoomModel> listRoom = new ArrayList<>();
            for (RoomDTO room:
                    postRequestDTO.getListRoom()) {
                RoomModel roomModel = new RoomModel();
                roomModel.setPostRoom(postModel);
                roomModel.setName(room.getRoomName());
                StatusModel status;
                if(room.isAvailableRoom()){
                    status = new StatusModel(1L);
                }else{
                    status = new StatusModel(2L);
                }
                roomModel.setStatus(status);
                listRoom.add(roomModel);
            }
            listRoom = roomRepository.saveAll(listRoom);
            postModel.setRoomNumber(postModel.getRoomNumber() + postRequestDTO.getListRoom().size());
            postRepository.save(postModel);
            return listRoom;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
