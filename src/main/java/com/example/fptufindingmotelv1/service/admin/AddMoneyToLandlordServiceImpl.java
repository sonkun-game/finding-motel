package com.example.fptufindingmotelv1.service.admin;

import com.example.fptufindingmotelv1.dto.PaymentDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.PaymentModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class AddMoneyToLandlordServiceImpl implements AddMoneyToLandlordService {
    @Autowired
    LandlordRepository landlordRepository;
    @Autowired
    PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public LandlordModel addMoneyForLandlord(PaymentDTO paymentDTO) {
        PaymentModel paymentModel = new PaymentModel();
        LandlordModel landlordModel = landlordRepository.findByUsername(paymentDTO.getLandlord());
        landlordModel.setAmount(landlordModel.getAmount() + paymentDTO.getAmount());
        landlordModel = landlordRepository.save(landlordModel);

        Date date = new Date();
        Date payDate = new Timestamp(date.getTime());
        paymentModel.setLandlordModel(landlordModel);
        paymentModel.setAmount(paymentDTO.getAmount());
        paymentModel.setPaymentMethod(paymentDTO.getPaymentMethod());
        paymentModel.setNote(paymentDTO.getNote());
        paymentModel.setPayDate(payDate);
        paymentRepository.save(paymentModel);
        return landlordModel;
    }
}
