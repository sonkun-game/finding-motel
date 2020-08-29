package com.example.fptufindingmotelv1.controller.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.service.landlord.manageownpost.EditPostService;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class EditPostController {

    @Autowired
    EditPostService editPostService;

    @ResponseBody
    @PostMapping("/api-edit-post")
    public JSONObject editPost(@RequestBody PostRequestDTO postRequestDTO) {
        PostModel postModel = editPostService.editPost(postRequestDTO);
        return postModel != null ?
                Constant.responseMsg("000", "Success", postModel.getId()) :
                Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }

    @ResponseBody
    @PostMapping("/api-get-images-by-post")
    public JSONObject getListImageByPost(@RequestBody PostRequestDTO postRequestDTO) {
        List<ImageModel> imageModels = editPostService.getListImageByPost(postRequestDTO);

        List<String> images = new ArrayList<>();
        for (ImageModel image:
                imageModels) {
            String imageUrl = "data:image/"+ image.getFileType()+";base64,"
                    + Base64.getEncoder().encodeToString(image.getFileContent());
            images.add(imageUrl);
        }

        return imageModels != null
                ? Constant.responseMsg("000", "Success", images)
                : Constant.responseMsg("999", "Lỗi hệ thống!", null);
    }
}
