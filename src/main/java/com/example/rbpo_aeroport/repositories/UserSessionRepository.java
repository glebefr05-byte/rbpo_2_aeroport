package com.example.rbpo_aeroport.repositories;

import com.example.rbpo_aeroport.entities.UserSession;
import com.example.rbpo_aeroport.models.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByRefreshTokenAndStatus(String refreshToken, SessionStatus status);

    List<UserSession> findByUserIAndStatus(UUID userId, SessionStatus status);

    @Modifying
    @Query("UPDATE UserSession us SET us.status = :status, us.revokedAt = revokedAt WHERE us.id = :id")
    void updateSessionStatus(@Param("id") UUID id, @Param("status") SessionStatus status, @Param("revokedAt") Instant revokedAt);

    @Modifying
    @Query("UPDATE UserSession us SET us.status = 'EXPIRED' WHERE (us.accessTokenExpiry < :now OR us.refreshTokenExpiry) < :now AND us.status = 'ACTIVE'")
    void expireOldSessions(@Param("now") Instant now);

    boolean existsByRefreshToken(String refreshToken);

    Optional<UserSession>idById(UUID id);
}
