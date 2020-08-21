package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RoomModel;
import net.minidev.json.JSONObject;

import java.util.List;

public interface EditPostService {

    PostModel editPost(PostRequestDTO postRequestDTO);

    List<ImageModel> getListImageByPost(PostRequestDTO postRequestDTO);

}
