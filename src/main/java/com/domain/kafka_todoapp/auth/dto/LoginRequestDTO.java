package com.domain.kafka_todoapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


public record LoginRequestDTO(@NotBlank(message = "Username is required.")
                              String username,
                              @NotBlank(message = "Password is required.")
                              String password) {
}
