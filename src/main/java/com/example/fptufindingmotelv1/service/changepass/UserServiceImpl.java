package com.example.fptufindingmotelv1.service.changepass;

import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<UserModel> getAllUserModel() {
        return userRepository.findAll();
    }

    @Override
    public UserModel findOne(String username) {
        return userRepository.getOne(username);
    }

    @Override
    public void update(UserModel username) {
        userRepository.save(username);
    }
}
