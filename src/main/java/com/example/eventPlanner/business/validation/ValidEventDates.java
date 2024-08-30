package com.example.eventPlanner.business.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EventDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEventDates {

    String message() default "Invalid event dates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
