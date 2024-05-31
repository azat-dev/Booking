package com.azat4dev.booking.users.users_commands.presentation.api.rest.authentication.utils;

import com.azat4dev.booking.shared.domain.values.user.UserId;
import com.azat4dev.booking.users.common.presentation.security.services.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.function.Supplier;

@AllArgsConstructor
public final class AuthenticateCurrentSessionImpl implements AuthenticateCurrentSession {

    private final Log logger = LogFactory.getLog(this.getClass());

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

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", token));
            }

            return tokens;

        } catch (Exception failed) {
            this.securityContextHolderStrategy.clearContext();
            this.logger.trace("Failed to process authentication request", failed);

            throw failed;
        }
    }
}
