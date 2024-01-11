package com.example.security.repository;

import com.example.security.dao.Task;
import com.example.security.dao.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {


    @Override
    @EntityGraph(attributePaths = {"author", "executor"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Task> findAll(Specification<Task> spec, Pageable pageable);

    Page<Task> findAllByExecutorOrAuthor(User executor, User author, Pageable pageable);


}
