package com.example.fptufindingmotelv1.service.landlord.manageownpost;

import com.example.fptufindingmotelv1.dto.PostRequestDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.PaymentPostModel;
import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.repository.*;
import com.example.fptufindingmotelv1.untils.Constant;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service
public class ExtendTimePostServiceImpl implements ExtendTimePostService {

    @Autowired
    private PaymentPackageRepository paymentPackageRepository;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public JSONObject extendTimeOfPost(PostRequestDTO postRequestDTO) {
        LandlordModel landlordModel = landlordRepository.getLandlordById(postRequestDTO.getUsername());
        PaymentPackageModel packageModel
                = paymentPackageRepository.getPackageById(postRequestDTO.getPaymentPackageId());
        if((landlordModel.getAmount() - packageModel.getAmount()) < 0){
            return Constant.responseMsg("001", "Số tiền trong tài khoản không đủ", null);
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

}
