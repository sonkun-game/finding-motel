package com.example.fptufindingmotelv1.service.register;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.LandlordRepository;
import com.example.fptufindingmotelv1.repository.RenterRepository;
import com.example.fptufindingmotelv1.repository.RoleRepository;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RenterRepository renterRepository;
    @Autowired
    private LandlordRepository landlordRepository;
    @Autowired
    private UserRepository userRepository;
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
        UserModel registedModel = userRepository.save(userModel);
        if (registedModel != null ) {
            if (registedModel.getRole().getId() == 1) {
                RenterModel renterModel = new RenterModel();
                renterModel.setCareer("Hoc sinh");
                renterModel.setGender(true);
                renterRepository.save(renterModel);
            } else if(registedModel.getRole().getId() == 2) {
                LandlordModel landlordModel = new LandlordModel();
                landlordModel.setAmount(0);
                landlordRepository.save(landlordModel);
            }
        }
        return registedModel;
    }
}
