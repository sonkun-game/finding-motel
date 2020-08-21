package com.example.fptufindingmotelv1.controller.register;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.service.register.RegisterService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dang-ky")
    public String getRegister() {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            return "redirect:/";
        }
        return "register";
    }

    @ResponseBody
    @PostMapping("/register")
    public String register(@RequestBody UserDTO userDTO) {
        JSONObject registerMsg = new JSONObject();
        UserModel userModel = registerService.register(userDTO);
        if (userModel != null) {
            if (userModel.getFbAccount() != null && userModel.getFbAccount().length() != 0) {
                registerMsg.put("code", "003");
                registerMsg.put("message", "Register by Facebook success!");
            } else if (userModel.getGgAccount() != null && userModel.getGgAccount().length() != 0) {
                registerMsg.put("code", "002");
                registerMsg.put("message", "Register by Google success!");
            } else {
                registerMsg.put("code", "001");
                registerMsg.put("message", "Register success!");
            }
        }
        return registerMsg.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/check-existed-username", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isExistUsername(@RequestBody String username) {
        return userRepository.existsByUsername(username);
    }

    @ResponseBody
    @RequestMapping(value = "/check-existed-phone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isExistPhone(@RequestBody String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }

}



