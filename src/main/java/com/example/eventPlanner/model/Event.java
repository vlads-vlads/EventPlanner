package com.example.eventPlanner.model;

import com.example.eventPlanner.business.validation.ValidEventDates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidEventDates
public class Event {

    @NotNull(message = "Event ID cannot be null")
    private Long id;

    @NotBlank(message = "Event title is required")
    @Size(min = 3, max = 100, message = "Event title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Event description is required")
    @Size(max = 1000, message = "Event description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Start date and time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @NotNull(message = "End date and time is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @NotNull(message = "Organizer ID cannot be null")
    private Long organizerId;

    @NotEmpty(message = "Event must have at least one tag")
    private List<String> tags;
}