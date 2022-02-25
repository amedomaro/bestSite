package com.bestSite.service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import com.bestSite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.bestSite.model.User> myUser = userRepository.findByUsername(username);
        if (myUser.get().isAccountNonLocked(myUser.get())) {
        return userRepository.findByUsername(username)
                        .map(user -> new User(user.getUsername(), user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("USER"))
                )).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new UsernameNotFoundException("User blocked");
        }
    }
}
