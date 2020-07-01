package com.example.fptufindingmotelv1.service.landlord;

import com.example.fptufindingmotelv1.model.PaymentPackageModel;
import com.example.fptufindingmotelv1.model.TypeModel;
import com.example.fptufindingmotelv1.repository.PaymentPackageRepository;
import com.example.fptufindingmotelv1.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagePostServiceImpl implements ManagePostService{

    @Autowired
    private PaymentPackageRepository paymentPackageRepository;

    @Autowired
    private TypeRepository typeRepository;

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
}
