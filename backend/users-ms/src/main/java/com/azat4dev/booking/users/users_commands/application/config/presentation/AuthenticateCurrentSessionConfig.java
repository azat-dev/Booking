package com.azat4dev.booking.users.users_commands.application.config.presentation;

import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtService;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils.AuthenticateCurrentSession;
import com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils.AuthenticateCurrentSessionImpl;
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
