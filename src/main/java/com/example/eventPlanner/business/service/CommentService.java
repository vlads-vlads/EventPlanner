package com.example.eventPlanner.business.service;

import com.example.eventPlanner.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAllComments();

    Optional<Comment> getCommentById(Long id);

    Comment saveComment(Comment comment);

    Comment updateComment(Comment comment, Long id);

    void deleteComment(Long id);

    List<Comment> getCommentsByEventId(Long eventId);

}
