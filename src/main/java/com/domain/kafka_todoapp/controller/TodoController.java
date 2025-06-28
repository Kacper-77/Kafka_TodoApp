package com.domain.kafka_todoapp.controller;

import com.domain.kafka_todoapp.db.todo.Todo;
import com.domain.kafka_todoapp.db.todo.TodoRepository;
import com.domain.kafka_todoapp.dto.TodoRequestDTO;
import com.domain.kafka_todoapp.enums.Priority;
import com.domain.kafka_todoapp.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/my-todos")
    public ResponseEntity<List<Todo>> getAllTodos() throws AccessDeniedException {
        List<Todo> todos = todoService.getAllTodos();

        return ResponseEntity.ok(todos);
    }

    @PostMapping("/add-todo")
    public ResponseEntity<Todo> addTodo(@RequestBody TodoRequestDTO dto, @RequestParam Priority priority) throws AccessDeniedException {
        Todo newTodo = todoService.addTodo(dto, priority);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
    }

    @PutMapping("/update-todo/{id}")
    public ResponseEntity<Todo> updateTodo(
            @RequestParam Long id,
            @RequestBody TodoRequestDTO dto,
            @RequestParam Priority priority) throws AccessDeniedException {
        Todo updatedTodo = todoService.updateTodo(id, dto, priority);

        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/delete-todo/{id}")
    public ResponseEntity<Void> deleteTodo(@RequestParam Long id) throws AccessDeniedException {
        todoService.deleteTodo(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/todo-toggle-complete/{id}")
    public ResponseEntity<Todo> toggleComplete(@RequestParam Long id) throws AccessDeniedException {
        Todo completedTodo = todoService.toggleComplete(id);

        return ResponseEntity.ok(completedTodo);
    }
}
