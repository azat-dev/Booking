package com.azat4dev.demobooking.users.common.presentation.security.services.jwt;

import com.azat4dev.demobooking.users.users_commands.domain.values.UserId;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String generateAccessToken(UserId userId, String[] authorities);

    boolean verifyToken(String authToken);

    String generateRefreshToken(UserId userId, String[] authorities);

    UserId getUserIdFromToken(String token) throws UserId.WrongFormatException;
}
