package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.model.GooglePojo;
import com.example.fptufindingmotelv1.model.UserModel;
import com.restfb.types.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface SocialLoginService {
    String googleLogin();

    String getGoogleToken(String code) throws IOException;

    GooglePojo getGgUserInfo(String accessToken) throws IOException;

    UserDetails buildUser(UserModel userModel);

    String facebookLogin();

    String getFacebookToken(String code) throws IOException;

    User getFbUserInfo(String accessToken) throws IOException;
}
