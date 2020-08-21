package com.example.fptufindingmotelv1.service.manageaccount;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public JSONObject savePassword(UserDTO request) {
        JSONObject jsonObject = new JSONObject();
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername());
            if((request.getPassword() == null || request.getPassword().isEmpty()) && !request.isHavePassword()){
                userModel.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(userModel);
                jsonObject.put("msgCode", "user000");
                jsonObject.put("message", "Thêm mật khẩu thành công");
                return jsonObject;
            }
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
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("msgCode", "sys999");
            return jsonObject;
        }

    }
}
