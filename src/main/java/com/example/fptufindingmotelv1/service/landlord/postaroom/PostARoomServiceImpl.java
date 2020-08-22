package com.example.fptufindingmotelv1.service.landlord.postaroom;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
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
public class PostARoomServiceImpl implements PostARoomService {

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
            return Constant.responseMsg("001", "Số tiền trong tài khoản không đủ", null);
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
            return Constant.responseMsg("001", "Bạn phải thêm tối thiếu 3 hình ảnh cho phòng trọ của mình", null);
        }else {
            imageRepository.saveAll(listImages);
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
        roomRepository.saveAll(listRoom);

        postRepository.save(newPostCreated);

        // save payment post
        PaymentPostModel paymentPostModel = new PaymentPostModel();
        paymentPostModel.setPaymentPackage(packageModel);
        paymentPostModel.setPostPayment(newPostCreated);
        paymentPostModel.setPayDate(createdDate);
        paymentPostRepository.save(paymentPostModel);

        landlordRepository.updateAmountLandlord(landlordModel.getAmount() - packageModel.getAmount(), landlordModel.getUsername());
        return Constant.responseMsg("000", "Success", postModel.getId());
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
