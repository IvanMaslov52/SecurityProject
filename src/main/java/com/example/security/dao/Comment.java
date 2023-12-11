package com.example.security.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Integer id;

    private String comment;
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
