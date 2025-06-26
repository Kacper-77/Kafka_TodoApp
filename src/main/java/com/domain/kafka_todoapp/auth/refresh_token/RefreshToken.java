package com.domain.kafka_todoapp.auth.refresh_token;

import com.domain.kafka_todoapp.db.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "Refresh_Token", nullable = false)
    private String refreshToken;

    @Column(name = "Expiry_Date", nullable = false)
    private LocalDateTime expiryDate;
}
