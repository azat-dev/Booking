package com.azat4dev.demobooking.users.presentation.api;

import com.azat4dev.demobooking.users.application.config.WebSecurityConfig;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.UsersService;
import com.azat4dev.demobooking.users.domain.values.UserId;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.FullName;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.LoginByEmailRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources.AuthenticationController;
import com.azat4dev.demobooking.users.presentation.security.entities.UserPrincipal;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import(WebSecurityConfig.class)
public class AuthenticationControllerTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UsersService usersService;

    @MockBean
    private JWTService tokenProvider;

    @Test
    void authenticate_givenUserNotExists_thenReturnError() throws Exception {

        // Given
        final var loginByEmailRequest = new LoginByEmailRequest(
            "email@company.com",
            "password"
        );

        // When
        final var response = performAuthenticateRequest(loginByEmailRequest);

        // Then
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
    }

    @Test
    void authenticate_givenExistingUserWrongPassword_thenReturnError() throws Exception {

        // Given
        final var user = givenExistingPrincipal();
        final var wrongPassword = user.getUsername() + "wrongPassword";

        final var authenticationRequest = new LoginByEmailRequest(
            user.getUsername(),
            wrongPassword
        );

        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("Wrong password"));

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
    }

    @Test
    void authenticate_givenEmptyPassword_thenReturnError() throws Exception {

        // Given
        final var user = givenExistingPrincipal();
        final var notValidPassword = "";

        final var authenticationRequest = new LoginByEmailRequest(
            user.getUsername(),
            notValidPassword
        );

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isBadRequest())
            .andExpect(unauthenticated());
    }


    @Test
    void singUp_givenNotMatchingPasswords_thenReturnError() throws Exception {

        // Given
        final var password1 = "password";
        final var password2 = "password2";
        final var validEmail = "valid@email.com";

        final SignUpRequest request = new SignUpRequest(
            new FullName(
                "John",
                "Doe"
            ),
            validEmail,
            password1,
            password2
        );

        // When
        final var response = performSignUpRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    private ResultActions performPostRequest(
        String url,
        Object request
    ) throws Exception {

        final var payload = objectMapper.writeValueAsString(request);

        return mockMvc.perform(
            post(url).contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        );
    }

    @Test
    void singUp_givenValidCredentials_thenCreateANewUser() throws Exception {

        // Given
        final var userId = UserId.generateNew();
        final var fullName = new FullName(
            "John",
            "Doe"
        );
        final var password = new EncodedPassword("password");
        final var validEmail = "valid@email.com";

        final var request = new SignUpRequest(
            fullName,
            validEmail,
            password.value(),
            password.value()
        );

        willDoNothing().given(usersService).handle(any());

        final var expectedAccessToken = "accessToken";
        final var expectedRefreshToken = "refreshToken";

        given(tokenProvider.generateAccessToken(any()))
            .willReturn(expectedAccessToken);

        given(tokenProvider.generateRefreshToken(any()))
            .willReturn(expectedRefreshToken);

        final var userPrincipal = new UserPrincipal(
            userId,
            password,
            List.of()
        );

        final var usernamePasswordToken = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(authenticationManager.authenticate(any())).willReturn(usernamePasswordToken);

        given(tokenProvider.generateAccessToken(userId))
            .willReturn(expectedAccessToken);

        given(tokenProvider.generateRefreshToken(userId))
            .willReturn(expectedRefreshToken);

        // When
        final var action = performSignUpRequest(request);

        // Then
        then(tokenProvider).should(times(1))
            .generateAccessToken(userId);

        then(tokenProvider).should(times(1))
            .generateRefreshToken(userId);

        then(passwordEncoder).should(times(1))
            .encode(eq(password.value()));

        action.andExpect(status().isCreated())
            .andExpect(authenticated());

        action.andExpect(jsonPath("$.userId").isNotEmpty())
            .andExpect(jsonPath("$.authenticationInfo.access").value(expectedAccessToken))
            .andExpect(jsonPath("$.authenticationInfo.refresh").value(expectedRefreshToken));

    }

    @Test
    void singUp_givenWrongDataFormat_thenReturnError() throws Exception {

        // Given
        final var request = new SignUpRequest(
            new FullName(
                "",
                ""
            ),
            "",
            "",
            ""
        );

        // When
        final var response = performSignUpRequest(request);

        // Then
        response.andExpect(status().isBadRequest());
    }

    // Helpers

    private UserPrincipal givenExistingPrincipal() {

        final var userId = UserId.generateNew();
        final var password = new EncodedPassword("password");

        final var userPrincipal = new UserPrincipal(
            userId,
            password,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        given(authenticationManager.authenticate(any())).willReturn(
            new UsernamePasswordAuthenticationToken(
                userPrincipal,
                password,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
            )
        );

        return userPrincipal;
    }

    private ResultActions performAuthenticateRequest(LoginByEmailRequest request) throws Exception {
        final String url = "/api/public/auth/token";
        return performPostRequest(
            url,
            request
        );
    }

    private ResultActions performSignUpRequest(SignUpRequest request) throws Exception {
        final String url = "/api/public/auth/sign-up";
        return performPostRequest(
            url,
            request
        );
    }
}
