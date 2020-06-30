package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.RenterModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ManageUserServiceImpl implements ManageUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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

    @Override
    public boolean savePhone(UserDTO request) {
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername());
            userModel.setPhoneNumber(request.getPhoneNumber());
            userRepository.save(userModel);
            return true;
        }catch (Exception exception){
            System.err.println(exception);
        }
        return false;
    }

    @Override
    public JSONObject savePassword(UserDTO request) {
        JSONObject jsonObject = new JSONObject();
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername());
            if(!passwordEncoder.matches(request.getPassword(), userModel.getPassword())){
                jsonObject.put("msgCode", "user001");
                jsonObject.put("message", "Mật khẩu hiện tại không chính xác");
                return jsonObject;
            }
            userModel.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userModel);
            jsonObject.put("msgCode", "user000");
            jsonObject.put("message", "Thay đổi mật khẩu thành công");
            return jsonObject;
        }catch (Exception exception){
            System.err.println(exception);
        }
        jsonObject.put("msgCode", "sys999");
        jsonObject.put("message", "System Error");
        return jsonObject;
    }
}
