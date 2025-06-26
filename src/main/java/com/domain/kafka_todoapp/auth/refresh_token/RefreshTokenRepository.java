package com.domain.kafka_todoapp.auth.refresh_token;

import com.domain.kafka_todoapp.db.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);
    void deleteByUser(User user);
    void deleteByRefreshToken(String token);
}
