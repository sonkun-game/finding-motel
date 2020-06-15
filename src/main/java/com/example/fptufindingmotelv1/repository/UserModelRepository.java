package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserModelRepository extends JpaRepository<UserModel, String> {

    UserModel findByGgAccount(String ggAccount);

    UserModel findByFbAccount(String fbAccount);

    UserModel findByUsername(String username);

    Boolean existsByUsername(String username);
    Boolean existsByPhoneNumber(String phone);

}
