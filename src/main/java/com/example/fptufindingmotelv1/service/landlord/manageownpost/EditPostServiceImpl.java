package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.ImageModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.ImageRepository;
import com.example.fptufindingmotelv1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class EditPostServiceImpl implements EditPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public PostModel editPost(PostRequestDTO postRequestDTO) {
        try {

            PostModel postModel = new PostModel(postRequestDTO.getPostId());

            imageRepository.deleteImagesByPost(postModel.getId());

            // save list image
            List<ImageModel> listImages = uploadImages(postRequestDTO.getListImage()
                    , postModel);

            listImages = imageRepository.saveAll(listImages);

            postModel.setImages(listImages);

            postRepository.updatePost(postRequestDTO.getTypeId(), postRequestDTO.getPrice(),
                    postRequestDTO.getDistance(), postRequestDTO.getSquare(), postRequestDTO.getDescription(),
                    postRequestDTO.getTitle(), postRequestDTO.getAddress(), postRequestDTO.getMapLocation(), postRequestDTO.getPostId());

            return postModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<ImageModel> getListImageByPost(PostRequestDTO postRequestDTO) {
        try {
            return imageRepository.getImageByPostId(postRequestDTO.getPostId());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<ImageModel> uploadImages(List<String> uploadImages, PostModel post){
        List<ImageModel> imageList = new ArrayList<>();
        for (int i = 0; i < uploadImages.size(); i++) {
            String imgBase64 = uploadImages.get(i);
            String fileType = "";
            if (StringUtils.isEmpty(imgBase64)) {
                return null;
            }else {
                fileType = imgBase64.split("/")[1].split(";")[0];
                imgBase64 = imgBase64.split(";base64,")[1];
            }
            byte[] fileBytes = Base64.getDecoder().decode(imgBase64);

            ImageModel imageModel = new ImageModel();

            imageModel.setFileContent(fileBytes);
            imageModel.setFileType(fileType);
            imageModel.setPost(post);
            imageList.add(imageModel);

        }
        return imageList;
    }
}
