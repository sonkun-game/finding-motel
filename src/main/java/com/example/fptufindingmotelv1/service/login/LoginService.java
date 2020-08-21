package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.model.GooglePojo;
import com.example.fptufindingmotelv1.model.UserModel;
import com.restfb.types.User;
import net.minidev.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface LoginService {
    LoginResponseDTO validateUser(String username, String password);

    String googleLogin();

    String getGoogleToken(String code) throws IOException;

    GooglePojo getGgUserInfo(String accessToken) throws IOException;

    UserDetails buildUser(UserModel userModel);

    String facebookLogin();

    String getFacebookToken(String code) throws IOException;

    User getFbUserInfo(String accessToken) throws IOException;

    public JSONObject getResponseGgLogin(String accessToken, HttpServletRequest request) throws IOException;
    public JSONObject getResponseFbLogin(String accessToken, HttpServletRequest request) throws IOException;
}
