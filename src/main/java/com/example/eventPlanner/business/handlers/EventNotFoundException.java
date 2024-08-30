package com.example.eventPlanner.business.handlers;

public class EventNotFoundException extends  RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}
