package com.example.security.controller;

import com.example.security.Filter.Filter;
import com.example.security.auth.TaskCreateRequest;
import com.example.security.auth.TaskUpdateRequest;
import com.example.security.dao.Task;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.NotEnoughRightsException;
import com.example.security.exception.NotFoundException;
import com.example.security.services.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;

    @GetMapping("/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    public ResponseEntity<Task> getTask(@PathVariable("id") Integer id) {
        return service.getTask(id);
    }

    @PostMapping
    @SecurityRequirement(name="Bearer Authentication")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@AuthenticationPrincipal UserDetails userDetails, @RequestBody TaskCreateRequest taskCreateRequest) {
        service.createTask(userDetails, taskCreateRequest);
    }


    @GetMapping()
    @SecurityRequirement(name="Bearer Authentication")
    public Page<Task> getAllTasks(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                  @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return service.getAllTasks(userDetails, offset, limit);
    }

    @GetMapping("/filter")
    @SecurityRequirement(name="Bearer Authentication")
    public Page<Task> filter(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                             @RequestParam(value = "limit", defaultValue = "20") Integer limit,
                             Filter filter) {
        return service.filter(userDetails, offset, limit, filter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name="Bearer Authentication")
    public void delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        service.deleteTask(id, userDetails);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name="Bearer Authentication")
    public void delete(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails, @RequestBody TaskUpdateRequest taskUpdateRequest) {
        service.updateTask(id, userDetails, taskUpdateRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e.getClass() == NotFoundException.class)
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        if (e.getClass() == BadRequestException.class)
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        if (e.getClass() == HttpMessageNotReadableException.class)
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        if (e.getClass() == NotEnoughRightsException.class)
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);
        System.out.println(e.getClass());
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
