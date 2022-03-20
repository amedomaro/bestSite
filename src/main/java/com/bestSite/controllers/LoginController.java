package com.bestSite.controllers;

import com.bestSite.model.User;
import com.bestSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginUser( Model model) {
        model.addAttribute("title", "Enter");
        return "login";
    }

    @GetMapping("/register")
    public String registrationUserGet(@ModelAttribute("user") User user){
        return "register";
    }

    @PostMapping("/register")
    public String registrationUserPost(@ModelAttribute("user") @Valid User newUser,
                                       BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "register";
        }
        if (!newUser.getPassword().equals(newUser.getRepeatPassword())){
            bindingResult.rejectValue("password","", "passwords not equals");
            return "register";
        }
        userService.register(newUser);
        return "redirect:/login";
    }
}
