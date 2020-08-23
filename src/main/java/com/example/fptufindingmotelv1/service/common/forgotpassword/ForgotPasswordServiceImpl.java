package com.example.fptufindingmotelv1.service.common.forgotpassword;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean saveNewPassword(UserDTO userDTO) {
        try {
            UserModel userModel = userRepository.findByPhoneNumber(userDTO.getPhoneNumber());
            if(userModel != null && userModel.getUsername() != null){
                userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                userRepository.save(userModel);
                return true;
            }
        }catch (Exception exception){
            System.err.println(exception);
        }
        return false;
    }
}
