package com.domain.kafka_todoapp.auth.service_and_controller;

import com.domain.kafka_todoapp.auth.jwt.JwtService;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshToken;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshTokenRepository;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshTokenService;
import com.domain.kafka_todoapp.custom_exceptions.TokenExpiredException;
import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.db.user.UserRepository;
import com.domain.kafka_todoapp.auth.dto.AuthResponseDTO;
import com.domain.kafka_todoapp.auth.dto.LoginRequestDTO;
import com.domain.kafka_todoapp.auth.dto.RefreshTokenDTO;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import com.domain.kafka_todoapp.utils.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            UserMapper userMapper,
            UserRepository userRepository,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RefreshTokenService refreshTokenService,
            RefreshTokenRepository refreshTokenRepository) {

        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public User registerUser(UserRequestDTO dto) {
        boolean isInDB = userRepository.findByUsername(dto.getUsername())
                .isPresent();

        if (isInDB) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User newUser = userMapper.toUserEntity(dto);
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(newUser);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        User user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        refreshTokenService.deleteByUser(user);

        UserRequestDTO claims = new UserRequestDTO(user);

        String token = jwtService.generateToken(claims);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponseDTO(token, refreshToken.getRefreshToken());
    }

    public void logout(String refreshToken) {
        var storedToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("Refresh token not found."));

        refreshTokenService.deleteByRefreshToken(storedToken.getRefreshToken());
    }

    public AuthResponseDTO getNewAccessToken(RefreshTokenDTO request) {
        String refreshToken = request.refreshToken();

        var storedToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("Refresh Token not found."));

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenService.deleteByRefreshToken(storedToken.getRefreshToken());
            throw new TokenExpiredException("Refresh token has been expired.");
        }

        String newAccesToken = jwtService.generateToken(storedToken.getUser());

        return new AuthResponseDTO(newAccesToken, refreshToken);
    }
}
