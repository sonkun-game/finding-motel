package com.example.fptufindingmotelv1.repository;

import com.example.fptufindingmotelv1.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByGgAccount(String ggAccount);
    UserModel findByFbAccount(String fbAccount);
    UserModel findByUsername(String username);
    UserModel findByPhoneNumber(String phoneNumber);

    Boolean existsByUsername(String username);
    Boolean existsByPhoneNumber(String phone);
    Boolean existsByGgAccount(String ggAccount);
    Boolean existsByFbAccount(String fbAccount);

    @Query("select u from UserModel u where u.username like %:username% or u.displayName like %:username%")
    ArrayList<UserModel> findByUsernameOrDisplayName(String username);
}
