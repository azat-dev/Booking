package com.azat4dev.booking.users.commands.infrastructure.presentation.api.rest.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.infrastructure.presentation.security.services.jwt.JwtService;
import io.micrometer.observation.annotation.Observed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.function.Supplier;

@Observed
@Slf4j
@AllArgsConstructor
public class AuthenticateCurrentSessionImpl implements AuthenticateCurrentSession {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    private final Supplier<JwtService> tokenProviderSupplier;
    private final Supplier<HttpServletRequest> currentRequestSupplier;
    private final Supplier<HttpServletResponse> currentResponseSupplier;

    private final SecurityContextRepository securityContextRepository;

    @Override
    public Tokens execute(
        UserId userId,
        String[] authorities
    ) {

        final var tokenProvider = tokenProviderSupplier.get();

        try {

            final var tokens = new Tokens(
                tokenProvider.generateAccessToken(userId, authorities),
                tokenProvider.generateRefreshToken(userId, authorities)
            );

            final var token = new BearerTokenAuthenticationToken(tokens.accessToken());

            final var context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(token);

            this.securityContextRepository.saveContext(
                context,
                currentRequestSupplier.get(),
                currentResponseSupplier.get()
            );

            log.atInfo().addArgument(userId).log("User authenticated: {}");
            return tokens;

        } catch (Exception e) {
            log.atError().setCause(e).log("Failed to process authentication request");
            this.securityContextHolderStrategy.clearContext();
            throw e;
        }
    }
}
