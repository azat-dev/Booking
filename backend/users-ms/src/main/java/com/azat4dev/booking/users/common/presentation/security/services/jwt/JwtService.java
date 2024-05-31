package com.azat4dev.booking.users.common.presentation.security.services.jwt;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {

    String generateAccessToken(UserId userId, String[] authorities);

    String generateRefreshToken(UserId userId, String[] authorities);
}
