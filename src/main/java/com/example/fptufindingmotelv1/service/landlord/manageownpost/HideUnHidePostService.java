package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.model.*;
import net.minidev.json.JSONObject;

import java.util.List;

public interface HideUnHidePostService {

    PostModel changePostStatus(PostRequestDTO postRequestDTO);
}
