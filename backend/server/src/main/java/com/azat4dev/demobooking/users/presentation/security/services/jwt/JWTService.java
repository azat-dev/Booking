package com.azat4dev.demobooking.users.presentation.security.services.jwt;

import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface JWTService {

    String generateAccessToken(UserId userId);

    String generateRefreshToken(UserId userId);

    boolean verifyToken(String authToken);

    UserId getUserIdFromToken(String token);
}
