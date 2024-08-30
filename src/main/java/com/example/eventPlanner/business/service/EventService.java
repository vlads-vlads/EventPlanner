package com.example.eventPlanner.business.service;

import com.example.eventPlanner.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> getAllEvents();

    Optional<Event> getEventById(Long id);

    Event saveEvent(Event event);

    Event updateEvent(Event event, Long id);

    void deleteEvent(Long id);

    void addParticipant(Long eventId);

    void removeParticipant(Long eventId);

    List<Event> getContentBasedRecommendations(Long userId);

    List<Event> getCollaborativeRecommendations(Long userId);

    List<Event> getHybridRecommendations(Long userId);
}
