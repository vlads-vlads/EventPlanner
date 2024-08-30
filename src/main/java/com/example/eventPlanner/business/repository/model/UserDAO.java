package com.example.eventPlanner.business.repository.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<EventDAO> organizedEvents;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentDAO> comments;

    @ManyToMany(mappedBy = "participants")
    private List<EventDAO> participatedEvents;
}
