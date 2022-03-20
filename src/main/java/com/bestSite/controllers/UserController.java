package com.bestSite.controllers;

import com.bestSite.model.Role;
import com.bestSite.model.Status;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;


@Controller
public class UserController {

    private User user;
    private final Role roleAdmin = new Role(2L, "ADMIN");
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-profile")
    public String showMyProfile(Model model) {
        user = userRepository.findByUsername(userService.getCurrentUser().getName()).orElseThrow();
        userService.showProfile(model, user);
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/my-profile/{id}")
    public String showUserProfile(@PathVariable(name = "id") long id, Model model) {
        user = userRepository.findById(id).orElseThrow();
        userService.showProfile(model, user);
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #user.getUsername().equals(authentication.name)")
    @PostMapping("/my-profile/{id}")
    public String editMyProfile(@PathVariable(name = "id") long id, @ModelAttribute("user") @Valid User user,
                                BindingResult bindingResult, @RequestParam Optional <MultipartFile> newAvatar) {
        if(bindingResult.hasErrors()) return "my-profile";
        userService.userUpdate(id, user, newAvatar);
        return "redirect:/my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/administration")
    public String showUsers(Model model) {
        Iterable<User> user = userRepository.findAll();
        model.addAttribute("user", user);
        return "administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/makeOrRemoveAdmin/{id}")
    public String makeOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        if (user.getRoles().contains(roleAdmin)) {
            user.removeRole(roleAdmin);
        } else {
            user.addRole(roleAdmin);
        }
        userRepository.save(user);
        return "redirect:/administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/blockUnblock/{id}")
    public String blockUnblock(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        user.setStatus(user.getStatus().equals(Status.BLOCKED) ? Status.ACTIVE : Status.BLOCKED);
        userRepository.save(user);
        return "redirect:/administration";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/administration";
    }
}