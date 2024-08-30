package com.example.eventPlanner.business.validation;

import com.example.eventPlanner.model.Event;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<ValidEventDates, Event> {

    @Override
    public void initialize(ValidEventDates constraintAnnotation) {
    }

    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        if (event.getStartDateTime() == null || event.getEndDateTime() == null) {
            return true;
        }

        boolean valid = true;

        if (event.getStartDateTime().isAfter(event.getEndDateTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date/time must be before end date/time")
                    .addPropertyNode("startDateTime")
                    .addConstraintViolation();
            valid = false;
        }

        if (event.getStartDateTime().isBefore(LocalDateTime.now())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Start date/time must be in the future")
                    .addPropertyNode("startDateTime")
                    .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}