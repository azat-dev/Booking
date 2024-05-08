package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto.CurrentUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UsersQueriesController implements UsersQueiresResource {

    @Override
    public ResponseEntity<CurrentUserDTO> getUser(JwtAuthenticationToken authentication) {
        return ResponseEntity.ok(new CurrentUserDTO("SOME ID", "example@gmail.com", new FullNameDTO("xyz", "yzzzz")));
    }
}
