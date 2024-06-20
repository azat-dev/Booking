package com.azat4dev.booking.users.config.users_commands.presentation;

import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtService;
import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils.AuthenticateCurrentSessionImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AuthenticateCurrentSessionConfig {

    @Autowired
    HttpServletRequest currentRequest;

    @Autowired
    HttpServletResponse currentRequestResponse;

    @Bean
    AuthenticateCurrentSession authenticateCurrentSession(
        JwtService jwtService,
        SecurityContextRepository securityContextRepository
    ) {
        return new AuthenticateCurrentSessionImpl(
            () -> jwtService,
            () -> currentRequest,
            () -> currentRequestResponse,
            securityContextRepository
        );
    }
}
