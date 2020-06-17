package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean validateUser(String username, String password) {
        try {
            UserModel userModel = userRepository.findByUsername(username);
            if(userModel != null && userModel.getUsername() != null && passwordEncoder.matches(password, userModel.getPassword())){
                return true;
            }
            return false;
        }catch (EntityNotFoundException entityNotFoundException){
            return false;
        }
    }
}
