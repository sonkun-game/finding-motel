package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.PostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ViewListOwnPostService {

    Page<PostModel> getAllPost(PostRequestDTO postRequestDTO, Pageable pageable);

}
