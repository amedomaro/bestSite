package com.bestSite.repository;

import com.bestSite.model.Comment;
import com.bestSite.model.Overview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByOverview(Overview overview);
}
