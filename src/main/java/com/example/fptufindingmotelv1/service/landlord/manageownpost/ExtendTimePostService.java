package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import net.minidev.json.JSONObject;

public interface ExtendTimePostService {

    JSONObject extendTimeOfPost(PostRequestDTO postRequestDTO);

}
