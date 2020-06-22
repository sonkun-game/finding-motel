package com.example.fptufindingmotelv1.service.displayall;

import com.example.fptufindingmotelv1.model.PostModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RenterServiceImpl implements RenterService{
    @Autowired
    RenterRepository renterRepository;
    @Override
    public RenterModel findOne(String username) {
        return renterRepository.findByUsername(username);
    }

}
