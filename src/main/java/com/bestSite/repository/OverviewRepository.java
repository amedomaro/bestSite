package com.bestSite.repository;

import com.bestSite.model.Overview;
import com.bestSite.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OverviewRepository extends JpaRepository<Overview, Long> {

    @EntityGraph(attributePaths = {"comments"})
    List<Overview> findAll();

    //Optional<Overview> findByAuthor(User author);
}
