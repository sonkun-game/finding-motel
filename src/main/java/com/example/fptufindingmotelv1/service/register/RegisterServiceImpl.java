package com.example.fptufindingmotelv1.service.register;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.repository.UserModelRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserModelRepository userModelRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public char[] generateOTP(int length) {
        char[] otp = new char[length];
        String numbers = "0123456789";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(rand.nextInt(numbers.length()));
        }
        return otp;
    }

    @Override
    public UserModel save(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userDTO.getUsername());
        userModel.setDisplayName(userDTO.getDisplayName());
        userModel.setFbAccount(userDTO.getFbAccount());
        userModel.setGgAccount(userDTO.getGgAccount());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setRole(roleRepository.getOne(Long.parseLong(userDTO.getRole().trim())));
        userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        System.err.println(userModel);
        return userModelRepository.save(userModel);
    }
}
