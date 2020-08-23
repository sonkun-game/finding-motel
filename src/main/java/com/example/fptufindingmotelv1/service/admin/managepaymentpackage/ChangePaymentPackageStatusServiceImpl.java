package com.example.fptufindingmotelv1.service.admin.managepaymentpackage;

import com.example.fptufindingmotelv1.dto.PaymentPackageDTO;
import com.example.fptufindingmotelv1.dto.StatusDTO;
import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.StatusModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.StatusRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChangePaymentPackageStatusServiceImpl implements ChangePaymentPackageStatusService{
    @Autowired
    StatusRepository statusRepository;

    @Autowired
    PaymentPackageRepository paymentPackageRepository;
    @Override
    public JSONObject getInitAdminManager() {
        JSONObject response = new JSONObject();
        try {
            // get list status report
            List<StatusModel> listStatus = statusRepository.findAllByType(2);
            List<StatusDTO> listStatusReport = new ArrayList<>();
            for (StatusModel status :
                    listStatus) {
                listStatusReport.add(new StatusDTO(status));
            }
            response.put("code", "000");
            response.put("listStatusReport", listStatusReport);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", "999");
            response.put("message", "Lỗi hệ thống!");
            return response;
        }
    }

    @Override
    public PaymentPackageModel changeStatusPaymentPackage(PaymentPackageDTO paymentPackageDTO) {
        try {
            PaymentPackageModel paymentPackageModel =
                    paymentPackageRepository.findById(paymentPackageDTO.getId()).get();
            paymentPackageModel.setAvailable(!paymentPackageModel.isAvailable());
            return paymentPackageRepository.save(paymentPackageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
