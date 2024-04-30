package com.azat4dev.demobooking.users.presentation.security.services;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        Assert.notNull(
            authenticationManager,
            "authenticationManager cannot be null"
        );
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        try {
            final var authRequest = getTokenFromRequest(request);
            if (authRequest == null) {
                this.logger.trace(
                    "Did not process authentication request since failed to find jwt token Authorization header"
                );
                chain.doFilter(
                    request,
                    response
                );
                return;
            }

            final var token = authRequest.getToken();

            if (!authenticationIsRequired(token)) {
                chain.doFilter(
                    request,
                    response
                );
                return;
            }

            Authentication authResult = this.authenticationManager.authenticate(authRequest);

            SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authResult);
            this.securityContextHolderStrategy.setContext(context);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(
                    LogMessage.format(
                        "Set SecurityContextHolder to %s",
                        authResult
                    )
                );
            }
            this.securityContextRepository.saveContext(
                context,
                request,
                response
            );
        } catch (AuthenticationException ex) {
            this.securityContextHolderStrategy.clearContext();
            this.logger.debug(
                "Failed to process authentication request",
                ex
            );
        }

        chain.doFilter(
            request,
            response
        );
    }

    protected boolean authenticationIsRequired(String token) {

        final var auth = this.securityContextHolderStrategy.getContext()
            .getAuthentication();

        if (!(auth instanceof JWTAuthenticationToken existingAuth)) {
            return true;
        }

        return !existingAuth.getToken()
            .equals(token) || !existingAuth.isAuthenticated();
    }

    private JWTAuthenticationToken getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        final var hasBearerToken = StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ");

        if (!hasBearerToken) {
            return null;
        }

        final var token = bearerToken.substring(
            7
        );
        return JWTAuthenticationToken.unauthenticated(token);
    }
}
