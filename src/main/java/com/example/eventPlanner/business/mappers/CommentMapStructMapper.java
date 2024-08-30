package com.example.eventPlanner.business.mappers;

import com.example.eventPlanner.business.repository.model.CommentDAO;
import com.example.eventPlanner.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CommentMapStructMapper {

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "user.id", target = "userId")
    Comment commentDAOToComment(CommentDAO commentDAO);

    @Mapping(source = "eventId", target = "event.id")
    @Mapping(source = "userId", target = "user.id")
    CommentDAO commentToCommentDAO(Comment comment);
}
