package com.example.rbpo_aeroport.controllers;

import com.example.rbpo_aeroport.entities.UserSession;
import com.example.rbpo_aeroport.models.RefreshRequest;
import com.example.rbpo_aeroport.models.enums.SessionStatus;
import com.example.rbpo_aeroport.repositories.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.rbpo_aeroport.configuration.JwtTokenProvider;
import com.example.rbpo_aeroport.entities.ApplicationUser;
import com.example.rbpo_aeroport.models.AuthenticationRequest;
import com.example.rbpo_aeroport.models.AuthenticationResponse;
import com.example.rbpo_aeroport.repositories.ApplicationUserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final ApplicationUserRepository applicationUserRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserSessionRepository userSessionRepository;

    @Value("${jwt.access.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            String email = request.getEmail();
            String password = request.getPassword();
            Optional<ApplicationUser> userOpt = applicationUserRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Неверные учетные данные"));
            }

            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Неверные учетные данные"));
            }

            ApplicationUser user = userOpt.get();
            String accessToken = jwtTokenProvider.generateAccessToken(user);
            String tempRefreshToken = jwtTokenProvider.generateRefreshToken(user, "temp");
            UserSession userSession = UserSession.builder()
                    .id(user.getId())
                    .refreshToken(tempRefreshToken)
                    .status(SessionStatus.ACTIVE)
                    .accessTokenExpiry(Instant.now().plusMillis(accessExpiration))
                    .build();

            UserSession savedSession = userSessionRepository.save(userSession);
            String finalRefreshToken = jwtTokenProvider.generateRefreshToken(user, savedSession.getId().toString());
            userSession.setRefreshToken(finalRefreshToken);
            userSessionRepository.save(userSession);

            return ResponseEntity.ok(new AuthenticationResponse(
                    email,
                    accessToken,
                    finalRefreshToken
            ));
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
        try {
            if (!jwtTokenProvider.validateRefreshToken(request.getRefreshToken())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Недействительный refresh token"));
            }

            String sessionId = jwtTokenProvider.getSessionIdFromRefreshToken(request.getRefreshToken());
            UUID userId = jwtTokenProvider.getUserIdFromRefreshToken(request.getRefreshToken());

            Optional<UserSession> sessionOpt = userSessionRepository.findById(UUID.fromString(sessionId));
            if (sessionOpt.isEmpty() || !sessionOpt.get().getStatus().equals(SessionStatus.ACTIVE)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Сессия не активна"));
            }

            UserSession oldSession = sessionOpt.get();
            if (!oldSession.getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Доступ запрещен"));
            }

            Optional<ApplicationUser> userOpt = applicationUserRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Пользователь не найден"));
            }

            ApplicationUser user = userOpt.get();


            oldSession.setStatus(SessionStatus.REVOKED);
            oldSession.setRevokedAt(Instant.now());
            userSessionRepository.save(oldSession);

            UserSession newSession = UserSession.builder()
                    .id(user.getId())
                    .status(SessionStatus.ACTIVE)
                    .refreshTokenExpiry(Instant.now().plusMillis(refreshExpiration))
                    .build();

            UserSession savedSession = userSessionRepository.save(newSession);
            String newAccessToken = jwtTokenProvider.generateAccessToken(user);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(user, savedSession.getId().toString());

            newSession.setRefreshToken(newRefreshToken);
            userSessionRepository.save(newSession);

            return ResponseEntity.ok(new AuthenticationResponse(
                    user.getEmail(),
                    newAccessToken,
                    newRefreshToken
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Ошибка обновления токена"));
        }
    }

}
