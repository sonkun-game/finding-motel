package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RentalRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
public class DeletePostServiceImpl implements DeletePostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PaymentPostRepository paymentPostRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Autowired
    private WishListRepository wishListRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public JSONObject deletePost(PostRequestDTO postRequestDTO) {
        if(postRequestDTO.getPostId() == null || postRequestDTO.getPostId().isEmpty()){
            return Constant.responseMsg("999", "NULL POST_ID", null);
        }
        // delete wishlist have post
        wishListRepository.deleteWishListsByPost(postRequestDTO.getPostId());

        // delete images of post
        imageRepository.deleteImagesByPost(postRequestDTO.getPostId());

        // delete rental requests of room of post
        rentalRequestRepository.deleteRentalRequestsByPost(postRequestDTO.getPostId());

        // delete rooms of post
        roomRepository.deleteRoomsByPost(postRequestDTO.getPostId());


        // delete paymentPosts  of post
        paymentPostRepository.deletePaymentPostsByPost(postRequestDTO.getPostId());


        // delete reports  of post
        reportRepository.deleteReportsByPost(postRequestDTO.getPostId());


        postRepository.deletePostById(postRequestDTO.getPostId());
        return Constant.responseMsg("000", "Success", null);

    }

}
