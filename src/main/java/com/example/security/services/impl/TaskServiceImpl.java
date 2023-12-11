package com.example.security.services.impl;

import com.example.security.Filter.Filter;
import com.example.security.auth.TaskCreateRequest;
import com.example.security.auth.TaskUpdateRequest;
import com.example.security.dao.Task;
import com.example.security.dao.User;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.NotEnoughRightsException;
import com.example.security.exception.NotFoundException;
import com.example.security.repository.TaskRepository;
import com.example.security.repository.UserRepository;
import com.example.security.services.TaskService;
import com.example.security.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import static com.example.security.Specification.TaskSpecification.*;
import static org.apache.logging.log4j.ThreadContext.isEmpty;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Override
    public ResponseEntity<Task> getTask(Integer id) {
        return ResponseEntity.ok(taskRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public void createTask(@AuthenticationPrincipal UserDetails userDetails, TaskCreateRequest taskCreateRequest) {
        if (taskCreateRequest == null)
            throw new BadRequestException();
        if (taskCreateRequest.getExecutor().isEmpty() || taskCreateRequest.getTitle().isEmpty()
                || taskCreateRequest.getDescription().isEmpty() || taskCreateRequest.getPriority().isEmpty())
            throw new BadRequestException();
        if (!EmailValidator.validate(taskCreateRequest.getExecutor()))
            throw new BadRequestException();
        User author = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NotFoundException::new);
        User executor = userRepository.findByEmail(taskCreateRequest.getExecutor()).orElseThrow(NotFoundException::new);
        Task task = Task.builder()
                .author(author)
                .executor(executor)
                .title(taskCreateRequest.getTitle())
                .description(taskCreateRequest.getDescription())
                .status("New")
                .priority(taskCreateRequest.getPriority())
                .build();
        taskRepository.save(task);
    }

    @Override
    public Page<Task> getAllTasks(UserDetails userDetails, Integer offset, Integer limit) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NotFoundException::new);
        return taskRepository.findAllByExecutorOrAuthor(user, user, PageRequest.of(offset, limit));

    }

    @Override
    public Page<Task> filter(UserDetails userDetails, Integer offset, Integer limit, Filter filter) {
        return taskRepository.findAll(
                Specification.where(statusEqual(filter.getStatus())
                        .and(executorEqual(filter.getExecutor())
                                .and(authorEqual(filter.getAuthor())
                                        .and(titleEqual(filter.getTitle())
                                                .and(descriptionEqual(filter.getDescription())
                                                        .and(priorityEqual(filter.getPriority()))))))),
                PageRequest.of(offset, limit));
    }

    @Override
    public void deleteTask(Integer id, UserDetails userDetails) {
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!task.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new NotEnoughRightsException();
        taskRepository.delete(task);
    }

    @Override
    public void updateTask(Integer id, UserDetails userDetails, TaskUpdateRequest taskUpdateRequest) {
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        if (!task.getAuthor().getUsername().equals(userDetails.getUsername()))
            throw new NotEnoughRightsException();
        User executor = userRepository.findByEmail(taskUpdateRequest.getExecutor()).orElseThrow(NotFoundException::new);
        task = Task.builder()
                .id(task.getId())
                .title(taskUpdateRequest.getTitle())
                .description(taskUpdateRequest.getDescription())
                .status(taskUpdateRequest.getStatus())
                .priority(taskUpdateRequest.getPriority())
                .author(task.getAuthor())
                .executor(executor)
                .build();
        taskRepository.save(task);
    }
}
