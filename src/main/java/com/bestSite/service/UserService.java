package com.bestSite.service;

import com.bestSite.model.Role;
import com.bestSite.model.Status;
import com.bestSite.model.User;
import com.bestSite.repository.RoleRepository;
import com.bestSite.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
}
