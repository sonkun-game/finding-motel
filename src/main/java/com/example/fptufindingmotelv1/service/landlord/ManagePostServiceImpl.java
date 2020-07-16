package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.dto.RoomDTO;
import com.example.fptufindingmotelv1.model.*;
import com.example.fptufindingmotelv1.repository.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.io.IOException;
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
    private StatusRepository statusRepository;

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
    private RenterRepository renterRepository;

    @Autowired
    private RentalRequestRepository rentalRequestRepository;

    @Override
    public List<PaymentPackageModel> getListPaymentPackage() {
        try {
            Sort sort = Sort.by("duration").ascending();
            return paymentPackageRepository.findAll(sort);
        }catch (Exception exception){
            System.err.println(exception);
        }
        return null;
    }

    @Override
    public List<TypeModel> getListTypePost() {
        try {
            return typeRepository.findAll();
        }catch (Exception exception){
            System.err.println(exception);
        }
        return null;
    }

    @Override
    public PostModel saveNewPost(PostRequestDTO postRequestDTO) {
        try {
            LandlordModel landlordModel = landlordRepository.findByUsername(postRequestDTO.getUsername());
            TypeModel typeModel = typeRepository.findById(postRequestDTO.getTypeId()).get();

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
            PaymentPackageModel packageModel
                    = paymentPackageRepository.findById(postRequestDTO.getPaymentPackageId()).get();
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
                    status = statusRepository.findByIdAndType(1, 1);
                }else{
                    status = statusRepository.findByIdAndType(2, 1);
                }
                roomModel.setStatus(status);
                roomModel.setPostRoom(newPostCreated);
                listRoom.add(roomModel);
            }
            listRoom = roomRepository.saveAll(listRoom);

            newPostCreated.setImages(listImages);
            newPostCreated.setRooms(listRoom);
            postRepository.save(newPostCreated);

            // save payment post
            PaymentPostModel paymentPostModel = new PaymentPostModel();
            paymentPostModel.setPaymentPackage(packageModel);
            paymentPostModel.setPostPayment(newPostCreated);
            paymentPostModel.setPayDate(createdDate);
            paymentPostRepository.save(paymentPostModel);

            landlordModel.setAmount(landlordModel.getAmount() - packageModel.getAmount());
            landlordRepository.save(landlordModel);
            return newPostCreated;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<PostModel> getAllPost(PostRequestDTO postRequestDTO) {
        try {
            LandlordModel landlordModel = landlordRepository.findByUsername(postRequestDTO.getUsername());
            return landlordModel.getPosts();
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

    @Override
    public PostModel extendTimeOfPost(PostRequestDTO postRequestDTO) {
        try {
            PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();
            LandlordModel landlordModel = landlordRepository.findByUsername(postRequestDTO.getUsername());
            PaymentPackageModel packageModel
                    = paymentPackageRepository.findById(postRequestDTO.getPaymentPackageId()).get();
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
            landlordModel.setAmount(landlordModel.getAmount() - packageModel.getAmount());
            landlordRepository.save(landlordModel);

            // save payment post
            PaymentPostModel paymentPostModel = new PaymentPostModel();
            paymentPostModel.setPaymentPackage(packageModel);
            paymentPostModel.setPostPayment(postModel);
            paymentPostModel.setPayDate(payDate);
            paymentPostRepository.save(paymentPostModel);

            // add expire date and save post
            postModel.setExpireDate(expireDate);
            postModel.setVisible(true);
            postModel = postRepository.save(postModel);
            return postModel;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public boolean deletePost(PostRequestDTO postRequestDTO) {
        PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();

        // delete wishlist have post
        for (RenterModel renter:
             postModel.getRenters()) {
            renter.getPosts().remove(postModel);
        }
        renterRepository.saveAll(postModel.getRenters());

        postModel.getRenters().clear();

        // delete images of post
        for (ImageModel image:
                postModel.getImages()) {
            imageRepository.delete(image);
        }

        // delete rooms of post
        for (RoomModel room:
             postModel.getRooms()) {
            if(room.getRoomRentals() != null && room.getRoomRentals().size() > 0){
                rentalRequestRepository.deleteAll(room.getRoomRentals());
            }
            roomRepository.delete(room);
        }

        // delete paymentPosts  of post
        for (PaymentPostModel paymentPostModel:
                postModel.getPaymentPosts()) {
            paymentPostRepository.delete(paymentPostModel);
        }

        // delete reports  of post
        for (ReportModel reportModel:
                postModel.getReports()) {
            reportRepository.delete(reportModel);
        }

        postRepository.delete(postModel);
        return true;

    }

    @Override
    public PostModel editPost(PostRequestDTO postRequestDTO) {
        try {
            LandlordModel landlordModel = landlordRepository.findByUsername(postRequestDTO.getUsername());
            TypeModel typeModel = typeRepository.findById(postRequestDTO.getTypeId()).get();

            PostModel postModel = postRepository.findById(postRequestDTO.getPostId()).get();
            postModel.setLandlord(landlordModel);
            postModel.setDescription(postRequestDTO.getDescription());
            postModel.setDistance(postRequestDTO.getDistance());
            postModel.setPrice(postRequestDTO.getPrice());
            postModel.setSquare(postRequestDTO.getSquare());
            postModel.setTitle(postRequestDTO.getTitle());
            postModel.setType(typeModel);


            for (int i = 0; i < postModel.getImages().size(); i++) {
                imageRepository.delete(postModel.getImages().get(i));
            }
            postModel.getImages().clear();

            // save list image
            List<ImageModel> listImages = uploadImages(postRequestDTO.getListImage()
                    , postModel);

            listImages = imageRepository.saveAll(listImages);

            postModel.setImages(listImages);

            PostModel newPostCreated = postRepository.save(postModel);

            return newPostCreated;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
