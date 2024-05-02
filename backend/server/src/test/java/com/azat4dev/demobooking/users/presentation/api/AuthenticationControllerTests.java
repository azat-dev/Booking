package com.azat4dev.demobooking.users.presentation.api;

import com.azat4dev.demobooking.users.application.config.WebSecurityConfig;
import com.azat4dev.demobooking.users.domain.UserHelpers;
import com.azat4dev.demobooking.users.domain.interfaces.services.EncodedPassword;
import com.azat4dev.demobooking.users.domain.services.UsersService;
import com.azat4dev.demobooking.users.domain.values.UserIdFactory;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.FullName;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.LoginByEmailRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.entities.SignUpRequest;
import com.azat4dev.demobooking.users.presentation.api.rest.authentication.resources.AuthenticationController;
import com.azat4dev.demobooking.users.presentation.security.entities.UserPrincipal;
import com.azat4dev.demobooking.users.presentation.security.services.CustomUserDetailsService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.presentation.security.services.jwt.UserIdNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static com.azat4dev.demobooking.users.domain.UserHelpers.anyValidUserId;
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
    private JwtService tokenProvider;

    @MockBean
    private JwtEncoder jwtEncoder;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private UserIdFactory userIdFactory;

    private String anyValidEmail() {
        return "email@company.com";
    }

    @Test
    void test_authenticate_givenUserNotExists_thenReturnError() throws Exception {

        // Given
        final var loginByEmailRequest = new LoginByEmailRequest(
            anyValidEmail(),
            "password"
        );

        given(customUserDetailsService.loadUserByEmail(any()))
            .willThrow(new UserIdNotFoundException("User not found"));

        // When
        final var response = performAuthenticateRequest(loginByEmailRequest);

        // Then
        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
    }

    @Test
    void test_authenticate_givenExistingUserWrongPassword_thenReturnError() throws Exception {

        // Given
        final var email = UserHelpers.anyValidEmail();
        final var user = givenExistingPrincipal();
        final var wrongPassword = "wrongPassword";
        final var wrongEncodedPassword = user.getPassword() + "wrong";

        final var authenticationRequest = new LoginByEmailRequest(
            email.getValue(),
            wrongPassword
        );

        given(customUserDetailsService.loadUserByEmail(any()))
            .willReturn(user);

        given(passwordEncoder.encode(any()))
            .willReturn(wrongEncodedPassword);

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        then(customUserDetailsService)
            .should(times(1))
            .loadUserByEmail(email);

        response.andExpect(status().isUnauthorized())
            .andExpect(unauthenticated());
    }

    @Test
    void test_authenticate_givenEmptyPassword_thenReturnError() throws Exception {

        // Given
        final var notValidPassword = "";
        final var validEmail = UserHelpers.anyValidEmail();

        final var authenticationRequest = new LoginByEmailRequest(
            validEmail.getValue(),
            notValidPassword
        );

        // When
        final var response = performAuthenticateRequest(authenticationRequest);

        // Then
        response.andExpect(status().isBadRequest())
            .andExpect(unauthenticated());
    }


    @Test
    void test_singUp_givenNotMatchingPasswords_thenReturnError() throws Exception {

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
    void test_singUp_givenValidCredentials_thenCreateANewUser() throws Exception {

        // Given
        final var userId = anyValidUserId();
        final var fullName = new FullName(
            "John",
            "Doe"
        );
        final var password = new EncodedPassword("password");
        final var validEmail = "valid@email.com";

        final var expectedAccessToken = "accessToken";
        final var expectedRefreshToken = "refreshToken";

        final var request = new SignUpRequest(
            fullName,
            validEmail,
            password.value(),
            password.value()
        );

        given(userIdFactory.generateNewUserId())
            .willReturn(userId);

        willDoNothing().given(usersService).handle(any());

        given(tokenProvider.generateAccessToken(any(), any()))
            .willReturn(expectedAccessToken);

        given(tokenProvider.generateRefreshToken(any(), any()))
            .willReturn(expectedRefreshToken);

        final var authentication = new BearerTokenAuthenticationToken(expectedAccessToken);

        given(authenticationManager.authenticate(any()))
            .willReturn(authentication);

        given(tokenProvider.generateAccessToken(any(), any()))
            .willReturn(expectedAccessToken);

        given(tokenProvider.generateRefreshToken(any(), any()))
            .willReturn(expectedRefreshToken);

        // When
        final var action = performSignUpRequest(request);

        // Then
        then(tokenProvider).should(times(1))
            .generateAccessToken(eq(userId), any());

        then(tokenProvider).should(times(1))
            .generateRefreshToken(eq(userId), any());

        then(passwordEncoder).should(times(1))
            .encode(eq(password.value()));

        action.andExpect(status().isCreated())
            .andExpect(authenticated());

        action.andExpect(jsonPath("$.userId").isNotEmpty())
            .andExpect(jsonPath("$.authenticationInfo.access").value(expectedAccessToken))
            .andExpect(jsonPath("$.authenticationInfo.refresh").value(expectedRefreshToken));

    }

    @Test
    void test_singUp_givenWrongDataFormat_thenReturnError() throws Exception {

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

        final var userId = anyValidUserId();
        final var password = new EncodedPassword("encodedPassword");

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
