package com.domain.kafka_todoapp.utils;

import com.domain.kafka_todoapp.db.todo.Todo;
import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.dto.TodoRequestDTO;
import com.domain.kafka_todoapp.enums.Priority;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {
    public Todo toTodoEntity(TodoRequestDTO dto, Priority priority, User user) {
        return new Todo(
                dto.title(),
                dto.description(),
                priority,
                user
        );
    }
}
