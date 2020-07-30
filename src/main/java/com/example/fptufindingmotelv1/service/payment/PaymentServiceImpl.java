package com.example.fptufindingmotelv1.service.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;


@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    LandlordRepository landlordRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public JSONObject savePayment(MomoResponseDTO momoResponseDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                //Get username of landlord
                LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
                if (momoResponseDTO.getErrorCode().equalsIgnoreCase("0")) {
                    jsonObject.put("code", "000");
                    jsonObject.put("message", "Nap tien thanh cong.");
                    //save payment
                    PaymentModel paymentModel = new PaymentModel();
                    paymentModel.setAmount(Float.parseFloat(momoResponseDTO.getAmount()));
                    paymentModel.setLandlordModel(landlordModel);
                    paymentModel.setMomoId(momoResponseDTO.getRequestId());
                    paymentModel.setPayDate(new Date());
                    paymentRepository.save(paymentModel);
                    //update landlord amount
                    float amount = landlordModel.getAmount();
                    amount += Float.parseFloat(momoResponseDTO.getAmount());
                    landlordModel.setAmount(amount);
                } else {
                    jsonObject.put("code", "001");
                    jsonObject.put("message", "Nap tien khong thanh cong : " + momoResponseDTO.getErrorCode());
                }

            }
        } catch (Exception e) {
            jsonObject.put("code", "999");
            jsonObject.put("message", "Loi he thong!");
        }
        return jsonObject;
    }
}
