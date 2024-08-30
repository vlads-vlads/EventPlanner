package com.example.eventPlanner.business.mappers;

import com.example.eventPlanner.business.repository.model.EventDAO;
import com.example.eventPlanner.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EventMapStructMapper {


    @Mapping(source = "organizer.id", target = "organizerId")
    Event eventDAOToEvent(EventDAO eventDAO);

    @Mapping(source = "organizerId", target = "organizer.id")
    EventDAO eventToEventDAO(Event event);
}