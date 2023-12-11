package com.example.security.repository;

import com.example.security.dao.Task;
import com.example.security.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface TaskRepository  extends JpaRepository<Task,Integer>, JpaSpecificationExecutor {
    Page<Task> findAllByExecutorOrAuthor(User executor,User author, Pageable pageable);

}
