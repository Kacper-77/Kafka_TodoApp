package com.domain.kafka_todoapp.auth.service_and_controller;

import com.domain.kafka_todoapp.auth.jwt.JwtService;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshToken;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshTokenRepository;
import com.domain.kafka_todoapp.auth.refresh_token.RefreshTokenService;
import com.domain.kafka_todoapp.db.user.User;
import com.domain.kafka_todoapp.db.user.UserRepository;
import com.domain.kafka_todoapp.dto.AuthResponseDTO;
import com.domain.kafka_todoapp.dto.LoginRequestDTO;
import com.domain.kafka_todoapp.dto.RefreshTokenDTO;
import com.domain.kafka_todoapp.dto.UserRequestDTO;
import com.domain.kafka_todoapp.utils.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        refreshTokenService.deleteByUser(user);

        UserRequestDTO claims = getClaims(user);

        String token = jwtService.generateToken(claims);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponseDTO(token, refreshToken.getRefreshToken());
    }

    public AuthResponseDTO getNewAccessToken(RefreshTokenDTO request) {
        String refreshToken = request.getRefreshToken();

        var newRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("Refresh Token not found."));

        String newAccesToken = jwtService.generateToken(newRefreshToken.getUser());

        return new AuthResponseDTO(newAccesToken, refreshToken);
    }

    private UserRequestDTO getClaims(User user) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUsername(user.getUsername());
        userRequestDTO.setAge(user.getAge());
        userRequestDTO.setEmail(user.getEmail());

        return userRequestDTO;
    }
}
