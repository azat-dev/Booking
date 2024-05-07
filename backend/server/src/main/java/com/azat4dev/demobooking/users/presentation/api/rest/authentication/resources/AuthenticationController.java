package com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.presentation.ControllerException;
import com.azat4dev.demobooking.common.presentation.ValidationException;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.domain.services.UsersService;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.domain.values.UserIdFactory;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.*;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.UserIdNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

@Component
public class AuthenticationController implements AuthenticationResource {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    @Autowired
    private UserIdFactory userIdFactory;

    @Autowired
    private JwtService tokenProvider;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<?> handleException(ValidationException ex) {
        return ex.toResponseEntity();
    }

    @ExceptionHandler({ControllerException.class})
    public ResponseEntity<?> handleException(ControllerException ex) {
        return ex.toResponseEntity();
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(
        SignUpRequest signUpRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        final var email = signUpRequest.parseEmail();
        final var password = signUpRequest.parsePassword();
        final var fullName = signUpRequest.parseFullName();

        final var encodedPassword = passwordService.encodePassword(password);
        final var userId = userIdFactory.generateNewUserId();

        try {
            usersService.handle(
                new CreateUser(
                    CommandId.generateNew(),
                    userId,
                    fullName,
                    email,
                    encodedPassword
                )
            );
        } catch (UsersService.UserWithSameEmailAlreadyExistsException e) {
            throw ControllerException.createError(
                HttpStatus.CONFLICT,
                "UserWithSameEmailAlreadyExists",
                e.getMessage()
            );
        }

        final var authorities = new String[]{"ROLE_USER"};
        final var authenticationResult = this.authenticateUser(userId, authorities, request, response);

        final var signUpResponse = new SignUpResponse(
            userId.toString(),
            authenticationResult
        );

        return ResponseEntity.created(URI.create("")).body(signUpResponse);
    }

    private GeneratedTokensDTO authenticateUser(
        UserId userId,
        String[] authorities,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        try {
            final var authenticationResponse = new GeneratedTokensDTO(
                tokenProvider.generateAccessToken(userId, authorities),
                tokenProvider.generateRefreshToken(userId, authorities)
            );

            final var token = new BearerTokenAuthenticationToken(authenticationResponse.access());

            final var context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(token);

            this.securityContextRepository.saveContext(
                context,
                request,
                response
            );

            if (this.logger.isDebugEnabled()) {
                this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", token));
            }

            return authenticationResponse;

        } catch (Exception failed) {
            this.securityContextHolderStrategy.clearContext();
            this.logger.trace("Failed to process authentication request", failed);

            throw failed;
        }
    }

    public ResponseEntity<LoginByEmailResponse> authenticate(
        @RequestBody LoginByEmailRequest authenticationRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        final var email = authenticationRequest.parseEmail();
        final var password = authenticationRequest.parsePassword();

        try {
            final var user = userDetailsService.loadUserByEmail(email);

            final var encodedPassword = passwordService.encodePassword(password);

            if (!encodedPassword.value().equals(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            final var authenticationResponse = authenticateUser(
                user.id(),
                new String[]{"USER_ROLE"},
                request,
                response
            );

            return ResponseEntity.ok(new LoginByEmailResponse(authenticationResponse));

        } catch (UserIdNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            throw e;
        }
    }
}
