package com.bestSite.service;

import com.bestSite.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class LoginService {

    private final UserService userService;

    @Autowired
    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public String registrationUser(User user, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "register";
        }
        if (userService.checkUser(user.getUsername())){
            bindingResult.rejectValue("username","","this username already exists");
            return "register";
        }
        if (user.getPassword().isEmpty() || !user.getPassword().equals(user.getRepeatPassword())){
            bindingResult.rejectValue("password","", "passwords not equals");
            return "register";
        }
        userService.register(user);
        return "redirect:/login";
    }
}
