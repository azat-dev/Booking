package com.azat4dev.demobooking.users.users_queries.presentation.api.rest;

import com.azat4dev.demobooking.users.common.domain.values.UserId;
import com.azat4dev.demobooking.users.common.presentation.security.services.jwt.JwtService;
import com.azat4dev.demobooking.users.users_commands.application.config.presentation.WebSecurityConfig;
import com.azat4dev.demobooking.users.users_commands.domain.UserHelpers;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources.UsersQueriesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersQueriesController.class)
@Import(WebSecurityConfig.class)
public class UsersQueriesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersQueryService usersService;

    @MockBean
    private JwtService tokenProvider;

    @MockBean
    private JwtEncoder jwtEncoder;

    @MockBean(name = "accessTokenDecoder")
    private JwtDecoder accessTokenDecoder;

    @Test
    public void test_getCurrentUserInfo_givenUserDoesNotExist_thenReturn404() throws Exception {

        // Given
        final var userId = UserHelpers.anyValidUserId();

        given(usersService.getPersonalInfoById(any())).willReturn(Optional.empty());

        // When
        final var result = performGetCurrentUserInfoRequest(Optional.of(userId));

        // Then
        result.andExpect(status().isNotFound());
    }

    @Test
    public void test_getCurrentUserInfo_givenUserIsNotAuthenticated_thenReturn401() throws Exception {

        // Given

        // When
        final var result = performGetCurrentUserInfoRequest(Optional.empty());

        // Then
        result.andExpect(status().isUnauthorized());
    }

    private ResultActions performGetCurrentUserInfoRequest(Optional<UserId> userId) throws Exception {
        final String url = "/api/with-auth/users/current";
        return performGetRequest(
            url,
            userId
        );
    }

    private ResultActions performGetRequest(
        String url,
        Optional<UserId> userId
    ) throws Exception {
        if (userId.isEmpty()) {
            return mockMvc.perform(
                get(url)
                    .with(csrf())
            );
        }
        return mockMvc.perform(
            get(url).with(jwt().jwt(c -> c.subject(userId.get().toString())))
                .with(csrf())
        );
    }
}
