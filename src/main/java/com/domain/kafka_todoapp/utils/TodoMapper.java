package com.domain.kafka_todoapp.utils;

import com.domain.kafka_todoapp.db.todo.Todo;
import com.domain.kafka_todoapp.dto.TodoRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {
    public Todo toTodoEntity(TodoRequestDTO dto) {
        return new Todo(
                dto.getTitle(),
                dto.getDescription()
        );
    }
}
