package com.bestSite.repository;

import com.bestSite.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long> {

    Optional<Page> findByName(String name);
}
