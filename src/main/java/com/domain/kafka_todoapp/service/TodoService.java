package com.domain.kafka_todoapp.service;

import com.domain.kafka_todoapp.db.todo.Todo;
import com.domain.kafka_todoapp.db.todo.TodoRepository;
import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.dto.TodoRequestDTO;
import com.domain.kafka_todoapp.enums.Priority;
import com.domain.kafka_todoapp.utils.TodoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;
    private final TodoMapper todoMapper;

    public TodoService(TodoRepository todoRepository, UserService userService, TodoMapper todoMapper) {
        this.todoRepository = todoRepository;
        this.userService = userService;
        this.todoMapper = todoMapper;
    }

    public List<Todo> getAllTodos() throws AccessDeniedException {
        User owner = userService.getCurrentUser();
        return owner.getTodosList() != null ? owner.getTodosList() : Collections.emptyList();
    }

    @Transactional
    public Todo addTodo(TodoRequestDTO dto, Priority priority) throws AccessDeniedException {
        User owner = userService.getCurrentUser();
        Todo newTodo = todoMapper.toTodoEntity(dto, priority, owner);

        return todoRepository.save(newTodo);
    }

    @Transactional
    public Todo updateTodo(Long todoId, TodoRequestDTO dto, Priority priority) throws AccessDeniedException {
        User owner = userService.getCurrentUser();

        Todo updatedTodo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("Todo with id: " + todoId + " doesn't exist."));

        updatedTodo.setTitle(dto.getTitle());
        updatedTodo.setDescription(dto.getDescription());
        updatedTodo.setPriority(priority);

        return todoRepository.save(updatedTodo);
    }

    @Transactional
    public void deleteTodo(Long todoId) throws AccessDeniedException {
        User owner = userService.getCurrentUser();
        Todo todoToDelete = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("Todo with id: " + todoId + " doesn't exist."));

        if (!todoToDelete.getUser().equals(owner)) {
            throw new AccessDeniedException("You are not owner of this todo.");
        }
        todoRepository.delete(todoToDelete);
    }

    public Todo toggleComplete(Long todoId) throws AccessDeniedException {
        User owner = userService.getCurrentUser();
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("Todo with id: " + todoId + " doesn't exist."));

        if (!todo.getUser().equals(owner)) {
            throw new AccessDeniedException("You are not owner of this todo.");
        }

        todo.setCompleted(!todo.getCompleted());
        return todoRepository.save(todo);
    }
}
