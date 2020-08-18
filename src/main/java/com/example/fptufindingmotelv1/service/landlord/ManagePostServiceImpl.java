package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.PostResponseDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ManagePostServiceImpl implements ManagePostService{

    @Autowired
    private PaymentPackageRepository paymentPackageRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private LandlordRepository landlordRepository;

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

    @Override
    public List<PaymentPackageModel> getListPaymentPackage(Boolean available) {
        try {
            return paymentPackageRepository.getListPaymentPackage(available);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TypeModel> getListTypePost() {
        try {
            return typeRepository.getAllTypeOfPost();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public JSONObject saveNewPost(PostRequestDTO postRequestDTO) {
        LandlordModel landlordModel = landlordRepository.getLandlordById(postRequestDTO.getUsername());
        PaymentPackageModel packageModel
                = paymentPackageRepository.getPackageById(postRequestDTO.getPaymentPackageId());
        if((landlordModel.getAmount() - packageModel.getAmount()) < 0){
            return responseMsg("001", "Số tiền trong tài khoản không đủ", null);
        }
        TypeModel typeModel = new TypeModel(postRequestDTO.getTypeId(), null);

        PostModel postModel = new PostModel();
        postModel.setLandlord(landlordModel);
        postModel.setDescription(postRequestDTO.getDescription());
        postModel.setDistance(postRequestDTO.getDistance());
        postModel.setPrice(postRequestDTO.getPrice());
        postModel.setSquare(postRequestDTO.getSquare());
        postModel.setTitle(postRequestDTO.getTitle());
        postModel.setVisible(true);
        postModel.setBanned(false);
        postModel.setRoomNumber(postRequestDTO.getRoomNumber());
        postModel.setType(typeModel);
        postModel.setAddress(postRequestDTO.getAddress());
        postModel.setMapLocation(postRequestDTO.getMapLocation());

        Date date = new Date();
        Date createdDate = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, packageModel.getDuration());
        Date expireDate = c.getTime();
        postModel.setCreateDate(createdDate);
        postModel.setExpireDate(expireDate);

        PostModel newPostCreated = postRepository.save(postModel);

        // save list image
        List<ImageModel> listImages = uploadImages(postRequestDTO.getListImage()
                , newPostCreated);
        if(listImages == null){
            return null;
        }else {
            listImages = imageRepository.saveAll(listImages);
        }

        // save list room
        List<RoomModel> listRoom = new ArrayList<>();
        for (RoomDTO room:
                postRequestDTO.getListRoom()) {
            RoomModel roomModel = new RoomModel();
            roomModel.setName(room.getRoomName());
            StatusModel status;
            if(room.isAvailableRoom()){
                status = new StatusModel(1L);
            }else{
                status = new StatusModel(2L);
            }
            roomModel.setStatus(status);
            roomModel.setPostRoom(newPostCreated);
            listRoom.add(roomModel);
        }
        listRoom = roomRepository.saveAll(listRoom);

        postRepository.save(newPostCreated);

        // save payment post
        PaymentPostModel paymentPostModel = new PaymentPostModel();
        paymentPostModel.setPaymentPackage(packageModel);
        paymentPostModel.setPostPayment(newPostCreated);
        paymentPostModel.setPayDate(createdDate);
        paymentPostRepository.save(paymentPostModel);

        landlordRepository.updateAmountLandlord(landlordModel.getAmount() - packageModel.getAmount(), landlordModel.getUsername());
        return responseMsg("000", "Success", postModel.getId());
    }

    @Override
    public List<PostModel> getAllPost(PostRequestDTO postRequestDTO) {
        try {
            return postRepository.getPostsByLandlord(postRequestDTO.getUsername());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PostModel changePostStatus(PostRequestDTO postRequestDTO) {
        try {
            PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();
            postModel.setVisible(postRequestDTO.getIsVisible());
            postModel = postRepository.save(postModel);
            return postModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public JSONObject extendTimeOfPost(PostRequestDTO postRequestDTO) {
        LandlordModel landlordModel = landlordRepository.getLandlordById(postRequestDTO.getUsername());
        PaymentPackageModel packageModel
                = paymentPackageRepository.getPackageById(postRequestDTO.getPaymentPackageId());
        if((landlordModel.getAmount() - packageModel.getAmount()) < 0){
            return responseMsg("001", "Số tiền trong tài khoản không đủ", null);
        }
        PostModel postModel = postRepository.getPostOnlyExpireDateById(postRequestDTO.getPostId());

        Date date = new Date();
        Date payDate = new Timestamp(date.getTime());
        Calendar c = Calendar.getInstance();
        if(payDate.after(postModel.getExpireDate())){
            c.setTime(payDate);
        }else{
            c.setTime(postModel.getExpireDate());
        }
        c.add(Calendar.MONTH, packageModel.getDuration());
        Date expireDate = c.getTime();

        // save amount of landlord
        landlordRepository.updateAmountLandlord(landlordModel.getAmount() - packageModel.getAmount(), landlordModel.getUsername());

        // save payment post
        PaymentPostModel paymentPostModel = new PaymentPostModel();
        paymentPostModel.setPaymentPackage(packageModel);
        paymentPostModel.setPostPayment(postModel);
        paymentPostModel.setPayDate(payDate);
        paymentPostRepository.save(paymentPostModel);

        // add expire date and save post
        postRepository.updateExpireDatePost(expireDate, true, postModel.getId());
        JSONObject response = new JSONObject();

        response.put("code", postModel != null ? "000" : "999");
        response.put("amount", landlordModel.getAmount() - packageModel.getAmount());
        response.put("expireDate", expireDate);
        return response;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public JSONObject deletePost(PostRequestDTO postRequestDTO) {
        if(postRequestDTO.getPostId() == null || postRequestDTO.getPostId().isEmpty()){
            return responseMsg("999", "NULL POST_ID", null);
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
        return responseMsg("000", "Success", null);

    }

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
    public JSONObject responseMsg(String code, String message, Object data) {
        JSONObject msg = new JSONObject();
        msg.put("code", code);
        msg.put("message", message);
        msg.put("data", data);
        return msg;
    }
}
