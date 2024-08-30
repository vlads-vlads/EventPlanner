package com.example.eventPlanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @NotNull(message = "Comment ID cannot be null")
    private Long id;

    @NotBlank(message = "Comment content is required")
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    private String content;

    @NotNull(message = "Event ID cannot be null")
    private Long eventId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
}
