package com.domain.kafka_todoapp.db.todo;

import com.domain.kafka_todoapp.db.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUser(User user);
}
