package com.example.security.services;

import com.example.security.Filter.Filter;
import com.example.security.auth.TaskCreateRequest;
import com.example.security.auth.TaskUpdateRequest;
import com.example.security.dao.Task;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface TaskService {
    ResponseEntity<Task> getTask(Integer id);

    void createTask(UserDetails userDetails, TaskCreateRequest taskCreateRequest);

    Page<Task> getAllTasks(UserDetails userDetails, Integer offset, Integer limit);

    Page<Task> filter(UserDetails userDetails, Integer offset, Integer limit, Filter filter);


    void deleteTask(Integer id, UserDetails userDetails);

    void updateTask(Integer id, UserDetails userDetails, TaskUpdateRequest taskUpdateRequest);
}
