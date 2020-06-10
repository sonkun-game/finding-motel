package com.example.fptufindingmotelv1.service;

import org.springframework.social.google.api.plus.Person;

public interface GoogleService {
    String googleLogin();

    String getGoogleAccessToken(String code);

    Person getGoogleUserProfile(String accessToken);
}
