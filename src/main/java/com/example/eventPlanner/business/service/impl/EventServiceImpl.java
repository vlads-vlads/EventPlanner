package com.example.eventPlanner.business.service.impl;

import com.example.eventPlanner.business.handlers.EventNotFoundException;
import com.example.eventPlanner.business.mappers.EventMapStructMapper;
import com.example.eventPlanner.business.repository.EventRepository;
import com.example.eventPlanner.business.repository.UserRepository;
import com.example.eventPlanner.business.repository.model.EventDAO;
import com.example.eventPlanner.business.repository.model.UserDAO;
import com.example.eventPlanner.business.service.EventService;
import com.example.eventPlanner.model.Event;
import com.example.eventPlanner.security.SecurityUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventMapStructMapper eventMapper;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<Event> getAllEvents() {
        List<EventDAO> eventDAOList = eventRepository.findAll();
        log.info("Fetched all events. Total number of events: {}", eventDAOList::size);
        return eventDAOList.stream()
                .map(eventMapper::eventDAOToEvent)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id)
                .flatMap(eventDAO -> Optional.ofNullable(eventMapper.eventDAOToEvent(eventDAO)));
        log.info("Fetched event with id {}: {}", id, event);
        return event;
    }

    @Override
    public Event saveEvent(Event event) {
        EventDAO eventDAO = eventMapper.eventToEventDAO(event);
        EventDAO savedEventDAO = eventRepository.save(eventDAO);
        log.info("Saved new event with id {}", savedEventDAO.getId());
        return eventMapper.eventDAOToEvent(savedEventDAO);
    }

    @Override
    public Event updateEvent(Event event, Long id) {
        if (!eventRepository.existsById(id)) {
            log.error("Event with id {} not found", id);
            throw new EventNotFoundException("Event with id " + id + " not found");
        }

        EventDAO eventDAO = eventMapper.eventToEventDAO(event);
        eventDAO.setId(id);
        EventDAO updatedEventDAO = eventRepository.save(eventDAO);
        log.info("Updated event with id {}", id);
        return eventMapper.eventDAOToEvent(updatedEventDAO);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
        log.info("Deleted event with id {}", id);
    }

    @Override
    public void addParticipant(Long eventId) {
        Long authenticatedUserId = SecurityUtils.getAuthenticatedUserId();

        Optional<EventDAO> optionalEventDAO = eventRepository.findById(eventId);
        Optional<UserDAO> optionalUserDAO = userRepository.findById(authenticatedUserId);

        if (optionalEventDAO.isPresent() && optionalUserDAO.isPresent()) {
            EventDAO eventDAO = optionalEventDAO.get();
            UserDAO userDAO = optionalUserDAO.get();

            if (!eventDAO.getParticipants().contains(userDAO)) {
                eventDAO.getParticipants().add(userDAO);
                userDAO.getParticipatedEvents().add(eventDAO);
                eventRepository.save(eventDAO);
                userRepository.save(userDAO);
                log.info("Added user with id {} to event with id {}", authenticatedUserId, eventId);
            } else {
                log.info("User with id {} is already a participant of event with id {}", authenticatedUserId, eventId);
            }
        } else {
            log.error("Event with id {} or User with id {} not found", eventId, authenticatedUserId);
            throw new RuntimeException("Event or User not found");
        }
    }

    @Override
    public void removeParticipant(Long eventId) {
        Long authenticatedUserId = SecurityUtils.getAuthenticatedUserId();

        Optional<EventDAO> optionalEventDAO = eventRepository.findById(eventId);
        Optional<UserDAO> optionalUserDAO = userRepository.findById(authenticatedUserId);

        if (optionalEventDAO.isPresent() && optionalUserDAO.isPresent()) {
            EventDAO eventDAO = optionalEventDAO.get();
            UserDAO userDAO = optionalUserDAO.get();

            if (eventDAO.getParticipants().contains(userDAO)) {
                eventDAO.getParticipants().remove(userDAO);
                userDAO.getParticipatedEvents().remove(eventDAO);
                eventRepository.save(eventDAO);
                userRepository.save(userDAO);
                log.info("Removed user with id {} from event with id {}", authenticatedUserId, eventId);
            } else {
                log.info("User with id {} is not a participant of event with id {}", authenticatedUserId, eventId);
            }
        } else {
            log.error("Event with id {} or User with id {} not found", eventId, authenticatedUserId);
            throw new RuntimeException("Event or User not found");
        }
    }

    @Override
    public List<Event> getContentBasedRecommendations(Long userId) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<String> userTags = user.getParticipatedEvents().stream()
                .flatMap(eventDAO -> eventDAO.getTags().stream())
                .collect(Collectors.toSet());

        return eventRepository.findAll().stream()
                .filter(eventDAO -> !user.getParticipatedEvents().contains(eventDAO))
                .filter(eventDAO -> !Collections.disjoint(eventDAO.getTags(), userTags))
                .map(eventMapper::eventDAOToEvent)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getCollaborativeRecommendations(Long userId) {
        UserDAO user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserDAO> allUsers = userRepository.findAll();
        Map<UserDAO, Double> userSimilarities = new HashMap<>();

        for (UserDAO otherUser : allUsers) {
            if (!otherUser.equals(user)) {
                double similarity = calculateSimilarity(user, otherUser);
                userSimilarities.put(otherUser, similarity);
            }
        }

        List<UserDAO> similarUsers = userSimilarities.entrySet().stream()
                .sorted(Map.Entry.<UserDAO, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Set<EventDAO> recommendedEvents = new HashSet<>();
        for (UserDAO similarUser : similarUsers) {
            recommendedEvents.addAll(similarUser.getParticipatedEvents());
        }

        recommendedEvents.removeAll(user.getParticipatedEvents());
        return recommendedEvents.stream()
                .map(eventMapper::eventDAOToEvent)
                .collect(Collectors.toList());
    }

    private double calculateSimilarity(UserDAO user1, UserDAO user2) {
        Set<EventDAO> user1Events = new HashSet<>(user1.getParticipatedEvents());
        Set<EventDAO> user2Events = new HashSet<>(user2.getParticipatedEvents());

        Set<EventDAO> intersection = new HashSet<>(user1Events);
        intersection.retainAll(user2Events);

        double similarity = (double) intersection.size() / (user1Events.size() + user2Events.size() - intersection.size());
        return similarity;
    }

    @Override
    public List<Event> getHybridRecommendations(Long userId) {
        List<Event> contentBased = getContentBasedRecommendations(userId);
        List<Event> collaborative = getCollaborativeRecommendations(userId);

        Set<Event> combined = new HashSet<>(contentBased);
        combined.addAll(collaborative);

        return new ArrayList<>(combined);
    }
}
