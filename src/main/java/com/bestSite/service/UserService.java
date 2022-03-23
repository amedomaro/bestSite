package com.bestSite.service;

import com.bestSite.model.Overview;
import com.bestSite.model.Role;
import com.bestSite.model.Status;
import com.bestSite.model.User;
import com.bestSite.repository.OverviewRepository;
import com.bestSite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private User user;
    private final Role roleAdmin = new Role(2L, "ADMIN");
    private final UserRepository userRepository;
    private final OverviewRepository overviewRepository;
    private final CloudService cloudService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, OverviewRepository overviewRepository, CloudService cloudService,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.overviewRepository = overviewRepository;
        this.cloudService = cloudService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkUser(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(new Role(1L, "USER")));
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

    }

    public Model showUsers(Model model) {
        Iterable<User> user = userRepository.findAll();
        model.addAttribute("user", user);
        return model;
    }

    public String userUpdate(long id, User updatedUser, BindingResult bindingResult, Optional<MultipartFile> newAvatar) {
        if (bindingResult.hasErrors()) return "my-profile";
        user = userRepository.findById(id).orElseThrow();
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        if (cloudService.fileIsPresent(newAvatar.orElseThrow())) {
            String avatar = cloudService.uploadFile(newAvatar.orElseThrow());
            if (user.getAvatar() != null) cloudService.deleteFile(user.getAvatar());
            user.setAvatar(avatar);
        }
        userRepository.save(user);
        return "redirect:/my-profile";
    }

    public Model showProfile(Model model, User user) {
        Iterable<Overview> overviews = overviewRepository.findAllByAuthor(user);
        model.addAttribute("user", user);
        model.addAttribute("overviews", overviews);
        return model;
    }

    public void makeOrRemoveAdmin(long id) {
        user = userRepository.findById(id).orElseThrow();
        if (user.getRoles().contains(roleAdmin)) {
            user.removeRole(roleAdmin);
        } else {
            user.addRole(roleAdmin);
        }
        userRepository.save(user);
    }

    public void blockUnblock(long id) {
        user = userRepository.findById(id).orElseThrow();
        user.setStatus(user.getStatus().equals(Status.BLOCKED) ? Status.ACTIVE : Status.BLOCKED);
        userRepository.save(user);
    }

    public void delete(long id) {
        user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
    }

    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
