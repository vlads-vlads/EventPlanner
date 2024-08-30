package com.example.eventPlanner.business.service.impl;

import com.example.eventPlanner.business.handlers.CommentNotFoundException;
import com.example.eventPlanner.business.handlers.EventNotFoundException;
import com.example.eventPlanner.business.mappers.CommentMapStructMapper;
import com.example.eventPlanner.business.repository.CommentRepository;
import com.example.eventPlanner.business.repository.model.CommentDAO;
import com.example.eventPlanner.business.service.CommentService;
import com.example.eventPlanner.business.service.EventService;
import com.example.eventPlanner.model.Comment;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapStructMapper commentMapper;

    @Autowired
    private EventService eventService;

    @Override
    public List<Comment> getAllComments() {
        List<CommentDAO> commentDAOList = commentRepository.findAll();
        log.info("Fetched all comments. Total number of comments: {}", commentDAOList::size);
        return commentDAOList.stream()
                .map(commentMapper::commentDAOToComment)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Comment> getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id)
                .flatMap(commentDAO -> Optional.ofNullable(commentMapper.commentDAOToComment(commentDAO)));
        log.info("Fetched comment with id {}: {}", id, comment);
        return comment;
    }

    @Override
    public Comment saveComment(Comment comment) {
        CommentDAO commentDAO = commentMapper.commentToCommentDAO(comment);
        CommentDAO savedCommentDAO = commentRepository.save(commentDAO);
        log.info("Saved new comment with id {}", savedCommentDAO.getId());
        return commentMapper.commentDAOToComment(savedCommentDAO);
    }

    @Override
    public Comment updateComment(Comment comment, Long id) {
        if (!commentRepository.existsById(id)) {
            log.error("Comment with id {} not found", id);
            throw new CommentNotFoundException("Comment with id " + id + " not found");
        }

        CommentDAO commentDAO = commentMapper.commentToCommentDAO(comment);
        commentDAO.setId(id);
        CommentDAO updatedCommentDAO = commentRepository.save(commentDAO);
        log.info("Updated comment with id {}", id);
        return commentMapper.commentDAOToComment(updatedCommentDAO);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
        log.info("Deleted comment with id {}", id);
    }

    @Override
    public List<Comment> getCommentsByEventId(Long eventId) {
        eventService.getEventById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event with id " + eventId + " not found"));

        List<CommentDAO> commentDAOList = commentRepository.findByEventId(eventId);
        log.info("Fetched {} comments for event with id {}", commentDAOList.size(), eventId);
        return commentDAOList.stream()
                .map(commentMapper::commentDAOToComment)
                .collect(Collectors.toList());
    }
}
