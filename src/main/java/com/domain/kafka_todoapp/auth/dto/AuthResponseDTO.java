package com.domain.kafka_todoapp.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public record AuthResponseDTO(@NotBlank(message = "JWT token is required.") String token,
                              @NotBlank(message = "Refresh token is required.") String refreshToken) {

    public AuthResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
