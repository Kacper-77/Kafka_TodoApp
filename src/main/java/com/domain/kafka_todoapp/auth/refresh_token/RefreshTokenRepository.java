package com.domain.kafka_todoapp.auth.refresh_token;

import com.domain.kafka_todoapp.db.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);
    void deleteByUser(User user);
    void deleteByRefreshToken(String token);
}
