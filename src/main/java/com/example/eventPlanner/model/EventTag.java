package com.example.eventPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventTag {

    @NotNull(message = "Tag ID cannot be null")
    private Long id;

    @NotBlank(message = "Tag name is required")
    private String tag;

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;
}
