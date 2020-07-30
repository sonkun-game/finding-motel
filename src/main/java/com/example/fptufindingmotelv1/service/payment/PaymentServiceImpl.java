package com.example.fptufindingmotelv1.service.payment;

import com.example.fptufindingmotelv1.dto.MomoResponseDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    LandlordRepository landlordRepository;

    @Override
    public JSONObject savePayment(MomoResponseDTO momoResponseDTO) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken) {
                CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
                //Get username of landlord
                LandlordModel landlordModel = landlordRepository.findByUsername(userDetails.getUsername());
                if (momoResponseDTO.getErrorCode() == "0") {
                    jsonObject.put("code", "000");
                    jsonObject.put("message", "Nap tien thanh cong.");
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
