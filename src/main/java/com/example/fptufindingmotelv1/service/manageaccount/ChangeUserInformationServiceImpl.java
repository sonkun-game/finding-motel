package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChangeUserInformationServiceImpl implements ChangeUserInformationService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean saveUserInfo(UserDTO request) {
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername());
            userModel.setDisplayName(request.getDisplayName());
            if(userModel instanceof RenterModel){
                ((RenterModel) userModel).setCareer(request.getCareer());
                ((RenterModel) userModel).setDob(request.getDob());
                ((RenterModel) userModel).setGender(request.isGender());
            }
            userRepository.save(userModel);
            return true;
        }catch (Exception exception){
            System.err.println(exception);
        }
        return false;
    }

}
