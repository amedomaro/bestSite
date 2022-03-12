package com.bestSite.repository;

import com.bestSite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{

    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
