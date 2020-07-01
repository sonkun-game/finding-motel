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
import org.springframework.util.StringUtils;

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
            postModel.setRoomNumber(postRequestDTO.getRoomNumber());
            postModel.setType(typeModel);
            PaymentPackageModel packageModel
                    = paymentPackageRepository.findById(postRequestDTO.getPaymentPackageId()).get();
            Date date = new Date();
            Date createdDate = new Timestamp(date.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, packageModel.getDuration());
            Date expireDate = c.getTime();
            postModel.setCreateDate(createdDate);
            postModel.setExpireDate(expireDate);

            PostModel newPostCreated = postRepository.save(postModel);

            // save list image
            List<ImageModel> listImages = uploadImages(postRequestDTO.getListImage()
                    , postRequestDTO.getUsername(), newPostCreated);

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

    public List<ImageModel> uploadImages(List<String> uploadImages, String username, PostModel post){
        List<ImageModel> imageList = new ArrayList<>();
        // create user image folder
        String fileFolder = "src/main/resources/static/assets/img/rooms/" +username;
        File folder = new File(fileFolder);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.err.println("Failed to create new folder");
            }
        }
        for (int i = 0; i < uploadImages.size(); i++) {
            ImageModel imageModel = new ImageModel();
            String imgBase64 = uploadImages.get(i);
            StringBuffer fileName = new StringBuffer();
            fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
            if (StringUtils.isEmpty(imgBase64)) {
                return null;
            }
//            else if (imgBase64.indexOf("data:image/png;") != -1) {
//                imgBase64 = imgBase64.replace("data:image/png;base64,", "");
//                fileName.append(".png");
//            } else if (imgBase64.indexOf("data:image/jpeg;") != -1) {
//                imgBase64 = imgBase64.replace("data:image/jpeg;base64,", "");
//                fileName.append(".jpeg");
//            }
            else {
                String fileType = imgBase64.split("/")[1].split(";")[0];
                imgBase64 = imgBase64.split(";base64,")[1];
                fileName.append("." + fileType);
            }
            File file = new File(fileFolder + "/", fileName.toString());
            byte[] fileBytes = Base64.getDecoder().decode(imgBase64);
            try {
                FileUtils.writeByteArrayToFile(file, fileBytes);
                imageModel.setUrl("/assets/img/rooms/"+username+"/"+fileName.toString());
                imageModel.setPost(post);
                imageList.add(imageModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageList;
    }
}
