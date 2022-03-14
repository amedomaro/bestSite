package com.bestSite.controllers;

import com.bestSite.model.Overview;
import com.bestSite.model.Role;
import com.bestSite.model.Status;
import com.bestSite.model.User;
import com.bestSite.repository.OverviewRepository;
import com.bestSite.repository.UserRepository;
import com.bestSite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@Controller
public class UserController {

    private User user;
    private final UserService userService;
    private final Role roleAdmin = new Role(2L, "ADMIN");
    private final UserRepository userRepository;
    private final OverviewRepository overviewRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository,
                          OverviewRepository overviewRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.overviewRepository = overviewRepository;
    }

    @GetMapping("/myProfile")
    public String showMyProfile(Model model) {
        user = userRepository.findByUsername(getCurrentUser().getName()).orElseThrow();
        userService.showProfile(model, user);
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/myProfile/{id}")
    public String showUserProfile(@PathVariable(name = "id") long id, Model model) {
        user = userRepository.findById(id).orElseThrow();
        userService.showProfile(model, user);
        return "my-profile";
    }

    @PreAuthorize("hasAuthority('ADMIN') or #username == authentication.name")
    @PostMapping("/myProfile")
    public String editMyProfile(@RequestParam String username, @RequestParam String firstname,
                                @RequestParam String lastname, @RequestParam String avatar,
                                @RequestParam String email) {
        user = userRepository.findByUsername(username).orElseThrow();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setAvatar(avatar);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/myProfile";
    }

    private Authentication getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/administration")
    public String showUsers(Model model) {
        Iterable<User> user = userRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("title", "All users");
        return "administration";
    }

    @RequestMapping(value = "/target", method = RequestMethod.GET)
    public String userTarget(HttpServletRequest request) {
        String[] checkBox = request.getParameterValues("checkBoxUser");
        String[] delete = request.getParameterValues("delete");
        String[] block = request.getParameterValues("block");
        String[] unlock = request.getParameterValues("unlock");
        if (checkBox == null) return "redirect:/administration";
        if (delete != null) userService.deleteUser(checkBox);
        if (block != null) userService.blockUser(checkBox);
        if (unlock != null) userService.unlockUser(checkBox);
        return "redirect:/administration";
    }


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

    @PostMapping("/users/blockUnblock/{id}")
    public String blockUnblock(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        user.setStatus(user.getStatus().equals(Status.BLOCKED) ? Status.ACTIVE : Status.BLOCKED);
        userRepository.save(user);
        return "redirect:/administration";
    }

    @PostMapping("/users/delete/{id}")
    public String delete(@PathVariable(value = "id") Long id) {
        user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/administration";
    }
}