package com.example.eventPlanner.business.repository;

import com.example.eventPlanner.business.repository.model.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserDAO, Long> {
    Optional<UserDAO> findByEmail(String username);
}
