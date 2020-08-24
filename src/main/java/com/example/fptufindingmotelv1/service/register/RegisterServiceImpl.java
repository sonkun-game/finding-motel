package com.example.fptufindingmotelv1.service.register;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.RoleModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RenterRepository renterRepository;
    @Autowired
    private LandlordRepository landlordRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserModel register(UserDTO userDTO) {
        try {
            RoleModel roleModel = new RoleModel(Long.parseLong(userDTO.getRole().trim()));
            UserModel userModel;
            if (roleModel != null && roleModel.getId() != null) {
                if (roleModel.getId() == 1) {
                    userModel = passParamToUser(userDTO, roleModel);

                    renterRepository.save((RenterModel) userModel);
                    return userModel;

                } else if (roleModel.getId() == 2) {
                    userModel = passParamToUser(userDTO, roleModel);

                    if (userModel instanceof LandlordModel) {
                        ((LandlordModel) userModel).setAmount(0);
                    }
                    landlordRepository.save((LandlordModel) userModel);
                    return userModel;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public UserModel passParamToUser(UserDTO userDTO, RoleModel roleModel) {
        UserModel userModel;
        if (roleModel.getId() == 1) {
            userModel = new RenterModel();
        } else {
            userModel = new LandlordModel();
        }
        userModel.setUsername(userDTO.getUsername());
        userModel.setDisplayName(userDTO.getDisplayName());
        userModel.setFbAccount(userDTO.getFbAccount());
        userModel.setGgAccount(userDTO.getGgAccount());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setRole(roleModel);
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userModel;
    }
}
