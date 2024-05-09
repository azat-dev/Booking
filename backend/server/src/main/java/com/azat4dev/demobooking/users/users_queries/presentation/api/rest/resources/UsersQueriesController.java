package com.azat4dev.demobooking.users.users_queries.presentation.api.rest.resources;

import com.azat4dev.demobooking.users.users_commands.presentation.api.rest.authentication.entities.FullNameDTO;
import com.azat4dev.demobooking.users.users_queries.domain.services.UsersQueryService;
import com.azat4dev.demobooking.users.users_queries.presentation.api.rest.dto.PersonalUserInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UsersQueriesController implements UsersQueiresResource {

    @Autowired
    UsersQueryService usersQueryService;

    @Override
    public ResponseEntity<PersonalUserInfoDTO> getCurrentUserInfo(JwtAuthenticationToken authentication) {
        return ResponseEntity.ok(new PersonalUserInfoDTO("SOME ID", "example@gmail.com", new FullNameDTO("xyz", "yzzzz")));
    }
}
