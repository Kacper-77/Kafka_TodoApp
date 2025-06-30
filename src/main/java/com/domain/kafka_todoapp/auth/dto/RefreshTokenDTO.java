package com.domain.kafka_todoapp.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDTO(@NotBlank(message = "Refresh token is required.") String refreshToken) {
}
