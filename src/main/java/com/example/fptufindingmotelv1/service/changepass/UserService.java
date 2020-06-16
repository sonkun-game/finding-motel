package com.example.fptufindingmotelv1.service.changepass;

import com.example.fptufindingmotelv1.model.UserModel;

import java.util.List;

public interface UserService {
    public List<UserModel> getAllUserModel();
    public UserModel findOne(String username);
    public void update(UserModel username);
}
