package com.bestSite.controllers;

import com.bestSite.model.Role;
import com.bestSite.model.Status;
import com.bestSite.model.User;
import com.bestSite.repository.UserRepository;
import com.bestSite.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class UserController {

    private User user;
    private final Role role = new Role(2L, "ADMIN");
    private final UserAuthService userAuthService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserAuthService userAuthService, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse, UserRepository userRepository) {
        this.userAuthService = userAuthService;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.userRepository = userRepository;
    }

    @GetMapping("/myProfile")
    public String showMyProfile(Model model) {
        user = userRepository.findByUsername(getCurrentUser().getName()).orElseThrow();
        model.addAttribute("user", user);
        return "my-profile";
    }

    @PostMapping("/myProfile")
    public String editMyProfile(@RequestParam String username, @RequestParam String firstname,
                                @RequestParam String lastname, @RequestParam String avatar,
                                @RequestParam String email) {
        user = userRepository.findByUsername(getCurrentUser().getName()).orElseThrow();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setAvatar(avatar);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/myProfile";
    }

    @GetMapping("/allUsers")
    public String showUsers(Model model) {
        Iterable<User> user = userRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("title", "All users");
        return "all-users";
    }

    @RequestMapping(value = "/target", method = RequestMethod.GET)
    public String userTarget(HttpServletRequest request) {
        String[] checkBox = request.getParameterValues("checkBoxUser");
        String[] delete = request.getParameterValues("delete");
        String[] block = request.getParameterValues("block");
        String[] unlock = request.getParameterValues("unlock");
        if (checkBox == null) return "redirect:/allUsers";
        if (delete != null) deleteUser(checkBox);
        if (block != null) blockUser(checkBox);
        if (unlock != null) unlockUser(checkBox);
        return "redirect:/allUsers";
    }

    private String deleteUser(String[] checkBox) {
        for (String id : checkBox) {
            user = userRepository.findById(Long.parseLong(id)).orElseThrow();
            userRepository.delete(user);
        }
        return "redirect:/allUsers";
    }

    private String blockUser(String[] checkBox) {
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
        return "redirect:/allUsers";
    }

    private String unlockUser(String[] checkBox) {
        for (String id : checkBox) {
            user = userRepository.findById(Long.parseLong(id)).orElseThrow();
            user.setStatus(Status.ACTIVE);
            userRepository.save(user);
        }
        return "redirect:/allUsers";
    }

    private Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/users/makeOrRemoveAdmin/{id}")
    public String makeOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        if (user.getRoles().contains(role)) {
            user.removeRole(role);
        } else {
            user.addRole(role);
        }
        userRepository.save(user);
        return "redirect:/allUsers";
    }

    @PostMapping("/users/blockUnblock/{id}")
    public String blockUnblock(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        user.setStatus(user.getStatus().equals(Status.BLOCKED) ? Status.ACTIVE : Status.BLOCKED);
        userRepository.save(user);
        return "redirect:/allUsers";
    }

    @PostMapping("/users/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/allUsers";
    }
}