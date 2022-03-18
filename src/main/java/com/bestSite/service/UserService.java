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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    private User user;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final UserRepository userRepository;
    private final OverviewRepository overviewRepository;
    private final CloudService cloudService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                       UserRepository userRepository, OverviewRepository overviewRepository, CloudService cloudService, BCryptPasswordEncoder passwordEncoder) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.userRepository = userRepository;
        this.overviewRepository = overviewRepository;
        this.cloudService = cloudService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean checkUser(User user) {
        Optional<User> userFromDB = userRepository.findByUsername(user.getUsername());
        return userFromDB.isEmpty();
    }

    public void register(UserRegistration userRegistration){
        User user = new User();
        user.setUsername(userRegistration.getUsername());
        user.setFirstName(userRegistration.getFirstName());
        user.setLastName(userRegistration.getLastName());
        user.setEmail(userRegistration.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setRoles(Collections.singleton(new Role(1L, "USER")));
        user.setStatus(Status.ACTIVE);
        if(checkUser(user)){
            userRepository.save(user);
        }
    }

    public void userUpdate(long id, User updatedUser, Optional <MultipartFile> newAvatar){
        String avatar = cloudService.uploadFile(newAvatar.orElseThrow());
        user = userRepository.findById(id).orElseThrow();
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        if(user.getAvatar() != null) cloudService.deleteFile(user.getAvatar());
        user.setAvatar(avatar);
        userRepository.save(user);
    }

    public Model showProfile(Model model, User user) {
        Iterable<Overview> overviews = overviewRepository.findAllByAuthor(user);
        model.addAttribute("user", user);
        model.addAttribute("overviews", overviews);
        return model;
    }
    public Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String deleteUser(String[] checkBox) {
        for (String id : checkBox) {
            user = userRepository.findById(Long.parseLong(id)).orElseThrow();
            userRepository.delete(user);
        }
        return "redirect:/administration";
    }

    public String blockUser(String[] checkBox) {
        boolean isFlag = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (String id : checkBox) {
            user = userRepository.findById(Long.parseLong(id)).orElseThrow();
            user.setStatus(Status.BLOCKED);
            userRepository.save(user);
            if (user.getUsername().equals(auth.getName())) isFlag = true;
        }
        if (isFlag) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
            return "redirect:/login";
        }
        return "redirect:/administration";
    }

    public String unlockUser(String[] checkBox) {
        for (String id : checkBox) {
            user = userRepository.findById(Long.parseLong(id)).orElseThrow();
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/administration";
    }

}
