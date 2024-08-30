package com.example.eventPlanner.business.mappers;

import com.example.eventPlanner.business.repository.model.UserDAO;
import com.example.eventPlanner.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapStructMapper {


    User userDAOToUser(UserDAO userDAO);

    UserDAO userToUserDAO(User user);
}
