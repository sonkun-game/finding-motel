package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.dto.LoginDTO;
import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.GooglePojo;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.types.User;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SocialLoginServiceImpl implements SocialLoginService {

    @Autowired
    private Environment env;

    @Autowired
    private UserRepository userRepository;

    private GoogleConnectionFactory createGoogleConnection(){
        return new GoogleConnectionFactory(env.getProperty("google.app.id"), env.getProperty("google.app.secret"));
    }

    private FacebookConnectionFactory createFacebookConnection(){
        return new FacebookConnectionFactory(env.getProperty("facebook.app.id"), env.getProperty("facebook.app.secret"));
    }

    @Override
    public String googleLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(env.getProperty("google.redirect.uri"));
        parameters.setScope(env.getProperty("google.app.scope"));
        return createGoogleConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getGoogleToken(String code) throws IOException {
        String link = env.getProperty("google.link.get.token");
        String response = Request.Post(link)
                .bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
                        .add("client_secret", env.getProperty("google.app.secret"))
                        .add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }

    @Override
    public GooglePojo getGgUserInfo(String accessToken) throws IOException {
        try {
            String link = env.getProperty("google.link.get.user_info") + accessToken;
            String response = Request.Get(link).execute().returnContent().asString();
            JSONParser parser = new JSONParser();
            JSONObject jsonObjectResponse = (JSONObject) parser.parse(response);
            GooglePojo googlePojo = new GooglePojo();
            googlePojo.setId((String)jsonObjectResponse.get("id"));
            googlePojo.setName((String)jsonObjectResponse.get("name"));
            System.out.println(googlePojo);
            return googlePojo;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONObject getResponseGgLogin(String accessToken, HttpServletRequest request) throws IOException {

        GooglePojo googlePojo = getGgUserInfo(accessToken);
        UserModel userModel = userRepository.findByGgAccount(googlePojo.getId());
        if(userModel == null){
            userModel = new UserModel();
            userModel.setGgAccount(googlePojo.getId());
            userModel.setDisplayName(googlePojo.getName());
        }
        return getResponseLogin(accessToken, request, userModel);
    }

    public JSONObject getResponseLogin(String accessToken, HttpServletRequest request, UserModel userModel){
        JSONObject response = new JSONObject();
        if(userModel.getUsername() == null){
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setGgAccount(userModel.getGgAccount());
            loginDTO.setFbAccount(userModel.getFbAccount());
            loginDTO.setDisplayName(userModel.getDisplayName());
            response.put("user", loginDTO);
            response.put("msgCode", "msg003");
            response.put("message", "Not Registered Yet");
            response.put("accessToken", accessToken);
            return response;
        }else {
            UserDetails userDetails = buildUser(userModel);
            UsernamePasswordAuthenticationToken authentication = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("user", new LoginDTO(userModel));
            response.put("msgCode", "msg004");
            response.put("message", "Registered");
            response.put("accessToken", accessToken);
            return response;
        }
    }

    @Override
    public JSONObject getResponseFbLogin(String accessToken, HttpServletRequest request) throws IOException {
        User fbUser = getFbUserInfo(accessToken);
        UserModel userModel = userRepository.findByFbAccount(fbUser.getId());
        if(userModel == null){
            userModel = new UserModel();
            userModel.setFbAccount(fbUser.getId());
            userModel.setDisplayName(fbUser.getName());
        }
        return getResponseLogin(accessToken, request, userModel);
    }

    @Override
    public UserDetails buildUser(UserModel userModel) {
        return new CustomUserDetails(userModel);
    }

    @Override
    public String facebookLogin() {
        OAuth2Parameters parameters = new OAuth2Parameters();
        parameters.setRedirectUri(env.getProperty("facebook.redirect.uri"));
        parameters.setScope(env.getProperty("facebook.app.scope"));
        return createFacebookConnection().getOAuthOperations().buildAuthenticateUrl(parameters);
    }

    @Override
    public String getFacebookToken(String code) throws IOException {
        return createFacebookConnection().getOAuthOperations().
                exchangeForAccess(code, env.getProperty("facebook.redirect.uri"), null).getAccessToken();
    }

    @Override
    public com.restfb.types.User getFbUserInfo(String accessToken) throws IOException {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken,
                env.getProperty("facebook.app.secret"),
                Version.LATEST);
        return facebookClient.fetchObject("me", com.restfb.types.User.class);
    }
}
