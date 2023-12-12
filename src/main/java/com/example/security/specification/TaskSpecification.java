package com.example.security.specification;

import com.example.security.dao.Task;
import com.example.security.dao.Task_;
import com.example.security.dao.User;
import com.example.security.dao.User_;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> statusEqual(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isEmpty())
                return null;
            return cb.equal(root.get(Task_.status), status);
        };
    }

    public static Specification<Task> descriptionEqual(String description) {
        return (root, query, cb) -> {
            if (description == null || description.isEmpty())
                return null;
            return cb.like(root.get(Task_.description), "%" + description + "%");
        };
    }

    public static Specification<Task> titleEqual(String title) {
        return (root, query, cb) -> {
            if (title == null || title.isEmpty())
                return null;
            return cb.like(root.get(Task_.title), "%" + title + "%");
        };
    }

    public static Specification<Task> priorityEqual(String priority) {
        return (root, query, cb) -> {
            if (priority == null || priority.isEmpty())
                return null;
            return cb.equal(root.get(Task_.priority), priority);
        };
    }

    public static Specification<Task> authorEqual(String author) {
        return (root, query, cb) -> {
            if (author == null || author.isEmpty())
                return null;
            Join<Task, User> join = root.join(Task_.author);
            return cb.equal(join.get(User_.email), author);
        };
    }

    public static Specification<Task> executorEqual(String executor) {
        return (root, query, cb) -> {
            if (executor == null || executor.isEmpty())
                return null;
            Join<Task, User> join = root.join(Task_.executor);
            return cb.equal(join.get(User_.email), executor);
        };
    }
}
