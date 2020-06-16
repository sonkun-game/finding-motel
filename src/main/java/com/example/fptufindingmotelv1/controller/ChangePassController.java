package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.UserModel;
import com.example.fptufindingmotelv1.service.changepass.UserService;
import com.example.fptufindingmotelv1.service.register.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ChangePassController {
    @Autowired
    UserService userService;

    @GetMapping("/user/{username}/edit")
    public String edit(@PathVariable String username, Model model) {
        model.addAttribute("user", userService.findOne(username));
        return "qq";
    }
    @PostMapping("/user/save")
    public String save(
            @RequestParam(value = "user", required = false) String user,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "pass",required = false) String newpass,
            //@RequestParam(value = "phone",required = false) String phone,
            @RequestParam(name = "oldpass",required = false) String oldpass,
            //@RequestParam(value = "repass",required = false) String repass,
            Model model,
            @Valid UserModel userModel,

            BindingResult result,
            RedirectAttributes redirect) {

        if (result.hasErrors()) {
            return "changepass";
        }

        userService.update(userModel);
        return "redirect:/";
    }
}
