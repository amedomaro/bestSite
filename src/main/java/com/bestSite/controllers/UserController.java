package com.bestSite.controllers;

import com.bestSite.model.User;
import com.bestSite.repository.UserRepository;
import com.bestSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-profile")
    public String showMyProfile(Model model) {
        userService.showProfile(model, userRepository.findByUsername(userService.getCurrentUser().getName()).orElseThrow());
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/my-profile/{id}")
    public String showUserProfile(@PathVariable(name = "id") long id, Model model) {
        userService.showProfile(model, userRepository.findById(id).orElseThrow());
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.getUsername().equals(authentication.name)")
    @PostMapping("/my-profile/{user}")
    public String editMyProfile(@PathVariable User user, @Valid User newUser,
                                BindingResult bindingResult, @RequestParam Optional <MultipartFile> newAvatar) {
        return userService.userUpdate(user, bindingResult, newAvatar);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/administration")
    public String showUsers(Model model) {
        userService.showUsers(model);
        return "administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/makeOrRemoveAdmin/{id}")
    public String makeOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        userService.makeOrRemoveAdmin(id);
        return "redirect:/administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/blockUnblock/{id}")
    public String blockUnblock(@PathVariable(value = "id") Long id) {
        userService.blockUnblock(id);
        return "redirect:/administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);
        return "redirect:/administration";
    }
}