package com.domain.kafka_todoapp.auth.service_and_controller;

import com.domain.kafka_todoapp.auth.refresh_token.RefreshTokenRepository;
import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.dto.AuthResponseDTO;
import com.domain.kafka_todoapp.dto.LoginRequestDTO;
import com.domain.kafka_todoapp.dto.RefreshTokenDTO;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthController(AuthService authService, RefreshTokenRepository refreshTokenRepository) {
        this.authService = authService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostMapping("/login-page")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        AuthResponseDTO loggedUser = authService.login(dto);

        return ResponseEntity.ok(loggedUser);
    }

    @PostMapping("register-page")
    public ResponseEntity<User> register(@RequestBody UserRequestDTO dto) {
        User newUser = authService.registerUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDTO> getNewAccessToken(@RequestBody RefreshTokenDTO request) {
        AuthResponseDTO response = authService.getNewAccessToken(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);

        return ResponseEntity.noContent().build();
    }
}
