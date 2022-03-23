package com.bestSite.controllers;

import com.bestSite.model.User;
import com.bestSite.service.LoginService;
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

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
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
        return loginService.registrationUser(newUser, bindingResult);
    }
}
