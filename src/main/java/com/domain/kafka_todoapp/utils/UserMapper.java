package com.domain.kafka_todoapp.utils;

import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import com.domain.kafka_todoapp.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUserEntity(UserRequestDTO dto) {
        return new User(
                dto.getUsername(),
                dto.getEmail(),
                dto.getAge(),
                Role.USER
        );
    }
}
