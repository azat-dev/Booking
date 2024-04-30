package com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources;

import com.azat4dev.demobooking.common.CommandId;
import com.azat4dev.demobooking.common.DomainDataFormatException;
import com.azat4dev.demobooking.users.domain.commands.CreateUser;
import com.azat4dev.demobooking.users.domain.entities.FirstName;
import com.azat4dev.demobooking.users.domain.entities.FullName;
import com.azat4dev.demobooking.users.domain.entities.LastName;
import com.azat4dev.demobooking.users.domain.interfaces.services.PasswordService;
import com.azat4dev.demobooking.users.domain.services.UsersService;
import com.azat4dev.demobooking.users.domain.values.EmailAddress;
import com.azat4dev.demobooking.users.domain.values.Password;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.AuthenticationResponse;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpResponse;
import com.azat4dev.demobooking.users.presentation.security.entities.UserPrincipal;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@Component
public class AuthenticationController implements AuthenticationResource {

    @Autowired
    private JWTService tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UsersService usersService;

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception ex) {

        if (ex instanceof DomainDataFormatException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
        }

        return ResponseEntity.internalServerError().build();
    }

    @Override
    public ResponseEntity<SignUpResponse> signUp(
        @Valid SignUpRequest signUpRequest,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws DomainDataFormatException {

        final var password1 = signUpRequest.password1();
        final var password2 = signUpRequest.password2();

        if (!password1.equals(password2)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .build();
        }

        final var password = Password.makeFromString(password1);
        final var encodedPassword = passwordService.encodePassword(password);
        final var userId = UserId.generateNew();

        usersService.handle(
            new CreateUser(
                CommandId.generateNew(),
                userId,
                new FullName(
                    new FirstName(signUpRequest.fullName().firstName()),
                    new LastName(signUpRequest.fullName().lastName())
                ),
                EmailAddress.makeFromString(signUpRequest.email()),
                encodedPassword
            )
        );

        final var signUpResponse = new SignUpResponse(
            userId.toString(),
            new AuthenticationResponse(
                tokenProvider.generateAccessToken(userId),
                tokenProvider.generateRefreshToken(userId)
            )
        );

        this.authenticateUser(userId.toString(), password.getValue(), request, response);

        return ResponseEntity.created(URI.create("")).body(signUpResponse);
    }

    private AuthenticationResponse authenticateUser(
        String username,
        String password,
        HttpServletRequest request,
        HttpServletResponse response
    ) {

        final var authenticationRequest = new UsernamePasswordAuthenticationToken(
            username,
            password
        );

        final var authentication = authenticationManager.authenticate(authenticationRequest);
        final var userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final var userId = userPrincipal.id();

        final var authenticationResponse = new AuthenticationResponse(
            tokenProvider.generateAccessToken(userId),
            tokenProvider.generateRefreshToken(userId)
        );

        final var context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        this.securityContextRepository.saveContext(
            context,
            request,
            response
        );

        return authenticationResponse;
    }
}
