package com.domain.kafka_todoapp.controller;

import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import com.domain.kafka_todoapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-user")
    public ResponseEntity<User> updateUser(@RequestBody @Valid UserRequestDTO dto) throws AccessDeniedException {
        User updatedUser = userService.updateUser(dto);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) throws AccessDeniedException {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
