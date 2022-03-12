package com.bestSite.repository;

import com.bestSite.model.Overview;
import com.bestSite.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OverviewRepository extends JpaRepository<Overview, Long> {

    @EntityGraph(attributePaths = {"comments"})
    List<Overview> findAll();

    List<Overview> findAllByAuthor(User user);
}
