package com.example.fptufindingmotelv1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.api.plus.PlusOperations;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

@Service
public class GoogleServiceImpl implements GoogleService{

    @Value("${spring.social.google.app-id}")
    private String googleId;
    @Value("${spring.social.google.app-secret}")
    private String googleSecret;

    private GoogleConnectionFactory createGoogleConnection(){
        return new GoogleConnectionFactory(googleId, googleSecret);
    }

    @Override
    public String googleLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri("http://localhost:8081/google");
        parameters.setScope("profile email openid");
        return createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getGoogleAccessToken(String code) {
        return createGoogleConnection().getOAuthOperations().exchangeForAccess(code,"http://localhost:8081/google", null).getAccessToken();
    }

    @Override
    public Person getGoogleUserProfile(String accessToken) {
        Google google = new GoogleTemplate(accessToken);
        PlusOperations plusOperations = google.plusOperations();
        Person person = plusOperations.getGoogleProfile();
        return person;
    }
}
