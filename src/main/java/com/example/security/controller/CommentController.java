package com.example.security.controller;

import com.example.security.auth.CreateCommentRequest;
import com.example.security.dao.Comment;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.NotFoundException;
import com.example.security.services.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @GetMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    public Page<Comment> getComments(@PathVariable("id") Integer id,
                                     @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                     @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return service.getComments(id, offset, limit);
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    public void createComment(@AuthenticationPrincipal UserDetails userDetails,
                                       @RequestBody CreateCommentRequest createCommentRequest) {
         service.createComment(userDetails,createCommentRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        if (e.getClass() == NotFoundException.class)
            return new ResponseEntity<>("Not Found", HttpStatus.NOT_FOUND);
        if (e.getClass() == BadRequestException.class)
            return new ResponseEntity<>("Bad Request", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
