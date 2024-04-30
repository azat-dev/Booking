package com.azat4dev.demobooking.users.presentation.security.services.jwt;

import com.azat4dev.demobooking.users.domain.values.UserId;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {

    String generateAccessToken(UserId userId);

    String generateRefreshToken(UserId userId);

    boolean verifyToken(String authToken);

    UserId getUserIdFromToken(String token);
}
