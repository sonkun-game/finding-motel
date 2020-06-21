package com.example.fptufindingmotelv1.service.login;

import com.example.fptufindingmotelv1.dto.LoginResponseDTO;
import com.example.fptufindingmotelv1.model.LandlordModel;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Value("${ffm.date-format}")
    private String dateFormat;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO validateUser(String username, String password) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        LoginResponseDTO response = new LoginResponseDTO();
        try {
            UserModel userModel = userRepository.findByUsername(username);
            Date date = new Date();
            if (userModel == null || userModel.getUsername() == null){
                response.setMsgCode("login001");
                response.setMessage("Tên đăng nhập không tồn tại");
            }else if(userModel != null && userModel.getUsername() != null && !passwordEncoder.matches(password, userModel.getPassword())){
                response.setMsgCode("login002");
                response.setMessage("Mật khẩu không chính xác");
            }else if(userModel != null && userModel.getUsername() != null
                    && passwordEncoder.matches(password, userModel.getPassword())
                    && userModel instanceof LandlordModel && ((LandlordModel)userModel).getUnBanDate() != null
                    && ((LandlordModel)userModel).getUnBanDate().after(new Timestamp(date.getTime()))){
                response.setMsgCode("login003");
                response.setMessage("Tài khoản của bạn bị tạm khóa đến "+sdf.format(((LandlordModel)userModel).getUnBanDate()));
            }else {
                response.setMsgCode("login000");
                response.setMessage("Đăng nhập thành công!");
            }
            return response;
        }catch (EntityNotFoundException entityNotFoundException){
            return null;
        }
    }
}
