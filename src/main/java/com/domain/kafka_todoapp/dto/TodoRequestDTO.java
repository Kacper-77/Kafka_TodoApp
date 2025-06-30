package com.domain.kafka_todoapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public record TodoRequestDTO(@NotBlank(message = "Title is required.")
                             @Size(min = 1, max = 24, message = "Title must be 1-24 characters.")
                             String title,
                             @NotBlank(message = "Description is required.")
                             @Size(min = 1, max = 100, message = "Description must be 1-100 characters.")
                             String description) {
}
