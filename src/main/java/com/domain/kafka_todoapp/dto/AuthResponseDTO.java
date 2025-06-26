package com.domain.kafka_todoapp.dto;

import com.domain.kafka_todoapp.auth.refresh_token.RefreshToken;
import lombok.Getter;

@Getter
public class AuthResponseDTO {

    private String token;
    private String refreshToken;

    public AuthResponseDTO(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
