package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangePhoneNumberModel implements ChangePhoneNumberService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean savePhone(UserDTO request) {
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername());
            userModel.setPhoneNumber(request.getPhoneNumber());
            userRepository.save(userModel);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
