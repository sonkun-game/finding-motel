package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.model.CustomUserDetails;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(s);
        if(userModel == null){
            throw new UsernameNotFoundException(s);
        }
        return new CustomUserDetails(userModel);
    }
}
