package com.example.security.services;

import com.example.security.Filter.Filter;
import com.example.security.auth.CreateCommentRequest;
import com.example.security.dao.Comment;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

public interface CommentService {
    Page<Comment> getComments(Integer id,Integer offset, Integer limit);

    void createComment(UserDetails userDetails, CreateCommentRequest createCommentRequest);
}
