package com.example.rbpo_aeroport.entities;

import com.example.rbpo_aeroport.models.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userEmail;

    private String deviceId;

    @Column(length = 512)
    private String accessToken;

    @Column(length=512)
    private String refreshToken;

    private Instant accessTokenExpiry;

    private Instant refreshTokenExpiry;

    private Instant revokedAt;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}
