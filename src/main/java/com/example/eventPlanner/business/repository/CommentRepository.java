package com.example.eventPlanner.business.repository;

import com.example.eventPlanner.business.repository.model.CommentDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository <CommentDAO, Long> {
    List<CommentDAO> findByEventId(Long eventId);
}
