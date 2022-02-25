package com.bestSite.repository;

import com.bestSite.model.Overview;
import com.bestSite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OverviewRepository extends JpaRepository<Overview,Long> {
    Optional<User> findByAuthor(User author);
}
