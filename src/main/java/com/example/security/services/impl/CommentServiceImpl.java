package com.example.security.services.impl;

import com.example.security.auth.CreateCommentRequest;
import com.example.security.dao.Comment;
import com.example.security.dao.Task;
import com.example.security.dao.User;
import com.example.security.exception.BadRequestException;
import com.example.security.exception.NotFoundException;
import com.example.security.repository.CommentRepository;
import com.example.security.repository.TaskRepository;
import com.example.security.repository.UserRepository;
import com.example.security.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;
    @Override
    public Page<Comment> getComments(Integer id,Integer offset, Integer limit) {
        Task task = taskRepository.findById(id).orElseThrow(NotFoundException::new);
        return commentRepository.findAllByTask(task, PageRequest.of(offset, limit));
    }

    @Override
    public void createComment(UserDetails userDetails, CreateCommentRequest createCommentRequest) {
        Task task = taskRepository.findById(createCommentRequest.getId()).orElseThrow(NotFoundException::new);
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(NotFoundException::new);
        if(createCommentRequest.getDescription().isEmpty())
            throw new BadRequestException();
        Comment comment = Comment.builder()
                .comment(createCommentRequest.getDescription())
                .user(user)
                .task(task)
                .build();
        commentRepository.save(comment);
    }
}
