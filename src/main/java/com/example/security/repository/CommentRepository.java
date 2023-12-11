package com.example.security.repository;

import com.example.security.dao.Comment;
import com.example.security.dao.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAllByTask(Task task, Pageable pageable);
}
