package com.example.eventPlanner.business.repository;

import com.example.eventPlanner.business.repository.model.EventDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository <EventDAO, Long> {
}
